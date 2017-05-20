package com.mdm.dao.write.merchant;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mdm.exportBean.MerchantExport;
import com.mdm.importBean.MerchantCodeId;
import com.mdm.pojo.Merchant;
import com.mdm.pojo.MerchantCategory;
import com.mdm.response.MerchantResponse;

@Mapper
public interface MerchantWriteMapper {
	int addMerchant(Merchant merchant);
	
	int deleteById(Merchant merchant);
	
	int update(Merchant merchant);
	
	Merchant getMerchantById(@Param("id") String id);
	
	
	List<MerchantResponse> queryMerchantList( @Param("mName") String mName,@Param("code") String code
			,@Param("mManager") String mManager,@Param("contactTel") String contactTel,@Param("firstCategoryId") String firstCategoryId,
			@Param("status") String status, @Param("systemId") String systemId);
	List<Merchant> queryMerchantCheck(@Param("mName") String mName,@Param("code") String code);
	List<Merchant> batchQueryMerchantListById(List<String> list);

	List<MerchantCategory> queryMerchantType(@Param("parentId") String parentId);

	List<MerchantCategory> querySecondMerchantType();
	
	List<MerchantExport> exportMerchantList(@Param("mName") String mName,@Param("code") String code
			,@Param("mManager") String mManager,@Param("contactTel") String contactTel,@Param("firstCategoryId") String firstCategoryId,
			@Param("status") String status);
	
	List<MerchantCodeId> queryCodeIdMap();

	/**
	 * @param addList
	 * @return
	 */
	int batchInsert(@Param("list") List<Merchant> addList);

	/**
	 * @param updateList
	 */
	int batchUpdate(@Param("list") List<Merchant> updateList);
	
	/**
	 * 主要用户用户管理---商家树展示
	 * @Title: queryMerchantsForUser 
	 * @Description: TODO
	 * @return
	 * @return: List<MerchantResponse>
	 */
	List<MerchantResponse> queryMerchantsForUser();
	
	/**
	 * 更新积分账户
	 * @Title: updatePointAccount 
	 * @Description: TODO
	 * @param pointAccountId
	 * @param id
	 * @param orgType
	 * @param isExcepted
	 * @return
	 * @return: int
	 */
	int updatePointAccount(@Param("pointAccountId") String pointAccountId, @Param("id") String id, @Param("orgType") String orgType, @Param("isExcepted") boolean isExcepted, @Param("modifiedBy") String modifiedBy);
	/**
	 * 查询积分账户
	 * @Title: queryPointAccount 
	 * @Description: TODO
	 * @param id
	 * @return
	 * @return: Merchant
	 */
	Merchant queryPointAccount(@Param("id") String id);
	/**
	 * 通过积分账户查询商家
	 * @Title: queryMerchantIdByAccount 
	 * @Description: TODO
	 * @param pointAccountId
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String, Object>> queryMerchantIdByAccount(@Param("pointAccountId") String pointAccountId);
	/**
	 * 清空积分账户、是否排除
	 * @Title: resetPointAccount 
	 * @Description: TODO
	 * @param pointAccount
	 * @return
	 * @return: int
	 */
	int resetPointAccount(@Param("pointAccountId") String pointAccountId);
}
