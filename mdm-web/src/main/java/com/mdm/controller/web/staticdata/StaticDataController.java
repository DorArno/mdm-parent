/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年10月11日 下午1:58:00 *
**/
package com.mdm.controller.web.staticdata;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.mdm.controller.BaseController;
import com.mdm.core.bean.pojo.StaticData;
import com.mdm.core.service.sys.StaticDataService;
import com.mdm.core.util.MdmException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/staticdata")
public class StaticDataController extends BaseController {

	@Autowired
	private StaticDataService staticDataService;
	/**
	 * @author zhaoyao
	 * @for 查询数据字典列表
	 * @param tableName
	 * @param colName
	 * @param pageNum
	 * @param pageSize
	 * @return modelandview
	 * @throws MdmException
	 */
	@ApiOperation(value = "根据tablename查询列表", notes = "根据tablename查询列表")
	@RequestMapping(method = RequestMethod.GET)
	public Object queryStaticDataList(@RequestParam(value = "tableName", required = true) String tableName, @RequestParam(value = "colName", required = false) String colName) throws MdmException {
		return this.getResponseResult(staticDataService.queryStaticDataList(tableName, colName));
	}
	
    @RequestMapping(value="/staticdatas", method = RequestMethod.GET)
    public ModelAndView queryStaticDataList(@RequestParam(value = "tableName", required = false) String tableName, @RequestParam(value = "colName", required = false) String colName,
    		@RequestParam(value = "pageNum", required = true) Integer pageNum,
    		@RequestParam(value = "pageSize", required = true) Integer pageSize) throws MdmException{
    	return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(staticDataService.queryStaticDataList(tableName, colName,pageNum,pageSize)));
    }
    
    @RequestMapping(value="/staticdata", method = RequestMethod.POST)
    public ModelAndView addStaticdata(@RequestBody StaticData staticdata ) throws MdmException{
    	boolean isRepeat = checkRepeatStaticData(staticdata);
    	Map<String, Object> result = null;
    	if(isRepeat)
    	{
    		result= this.getFailResult("数据字典重复");
    	}else{
    		result=this.getResponseResult(staticDataService.addStaticdata(staticdata));
    	}      	
    	return new ModelAndView(new MappingJackson2JsonView(),result);
    }    
    
    
    /**
     * 判断是否是重复的数据字典
     * @param data
     * @return
     */
    private  boolean checkRepeatStaticData(StaticData data){
    	if(data == null)
    	{
    		return false;
    	}
    	List<StaticData>  staticDatas= this.staticDataService.queryStaticDataListEqual(data.getTableName(),data.getColName(),data.getColValue());
    	if(staticDatas.isEmpty())
    	{
    		return false;
    	}
    	
    	
    	
    	return true;
    	
    }
    
    @RequestMapping(value="/updateStaticdata/{id}", method = RequestMethod.PUT)
    public ModelAndView updateStaticdata(@RequestBody StaticData staticdata,@PathVariable String id ) throws MdmException{
    	staticdata.setId(id);
    	
    	StaticData  oldData = this.staticDataService.getById(id);
    	
    	Map<String, Object> result = null;
    	if(oldData !=null && !staticdata.getColValue().equals(oldData.getColValue()) && checkRepeatStaticData(staticdata))
    	{
    		result= this.getFailResult("数据字典重复");
    	}else{
    		result=this.getResponseResult(staticDataService.updateStaticdata(staticdata));
    	}      	
    	return new ModelAndView(new MappingJackson2JsonView(),result);
    }    
    
    @RequestMapping(value="/deleteStaticdata/{id}", method = RequestMethod.PUT)
    public ModelAndView deleteStaticdata(@RequestBody StaticData staticdata ,@PathVariable String id) throws MdmException{
    	staticdata.setId(id);
    	return new ModelAndView(new MappingJackson2JsonView(),this.getResponseResult(staticDataService.deleteStaticdata(staticdata)));
    }   
}
