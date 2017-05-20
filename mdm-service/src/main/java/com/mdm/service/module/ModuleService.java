package com.mdm.service.module;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdm.core.constants.GlobalConstants;
import com.mdm.dao.write.module.ModuleWriteMapper;
import com.mdm.pojo.Module;
import com.mdm.service.BaseService;

@Service
public class ModuleService extends BaseService<Module> {

	@Autowired
	private ModuleWriteMapper mapper;

	@Override
	public List<Module> queryList(Map<String, Object> params) {
		return mapper.queryList(params);
	}

	public int insert(Module dmo) {
		return mapper.insert(dmo);
	}

	@Transactional
	public int update(Module module, Module originalModule) {
		int result = mapper.update(module);
		// 如果修改了父节点，需要更新所有子节点路径
		if (!StringUtils.equals(module.getParentId(), originalModule.getParentId())) {
			if (StringUtils.startsWith(module.getParent().getPath(), originalModule.getPath())) {
				Module parent = getParent(originalModule.getParentId());
				List<Module> list = mapper.getChildren(originalModule.getId());
				for (Module childModule : list) {
					String childModuleOrigPath = childModule.getPath();
					int childModuleOrigLevel = childModule.getModuleLevel();
					// 更新直接子节点parentId、path和level
					childModule.setParentId(originalModule.getParentId());
					childModule.setPath(
							parent == null ? childModule.getId() : parent.getPath() + "/" + childModule.getId());
					childModule.setModuleLevel(parent == null ? 1 : parent.getModuleLevel() + 1);
					result += mapper.update(childModule);
					// 计算出层级差
					int levelDiff = childModule.getModuleLevel() - childModuleOrigLevel;
					// 更新子节点下所有子节点路径和层级
					result += mapper.updatePath(childModuleOrigPath, childModule.getPath(), levelDiff);
				}
			} else {
				int levelDiff = module.getModuleLevel() - originalModule.getModuleLevel();
				result += mapper.updatePath(originalModule.getPath(), module.getPath(), levelDiff);
			}
		}
		// 如果状态改变了，需要更新所有子节点状态
		if (!module.getModuleState().equals(originalModule.getModuleState())) {
			result += mapper.updateState(module.getModuleState(), module.getPath());
		}
		// 如果父节点处于停用状态，则所有子节点也修改为停用状态
		if (StringUtils.isNotBlank(module.getParentId())
				&& module.getParent().getModuleState().byteValue() == GlobalConstants.STATUS_STOP
				&& module.getModuleState().byteValue() == GlobalConstants.STATUS_START) {
			result += mapper.updateState(GlobalConstants.STATUS_STOP.intValue(), module.getPath());
		}
		return result;
	}

	public Module getById(String id) {
		return mapper.getById(id);
	}

	public Module getParent(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		return mapper.getParent(id);
	}

	public List<Module> queryAll(Map<String, Object> params) {
		return mapper.queryAll(params);
	}

	public int deleteByPath(Module module) {
		return mapper.deleteByPath(module);
	}

	public List<Module> queryModuleOperationList(Byte isDeleted) {
		return mapper.queryModuleOperationList(isDeleted);
	}

	public List<Module> queryUserRoleModuleList(Map<String, Object> params) {
		return mapper.queryUserRoleModuleList(params);
	}

}