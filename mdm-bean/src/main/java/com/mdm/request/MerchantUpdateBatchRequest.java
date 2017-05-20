package com.mdm.request;
/**
 * @author zhaoyao 2016/11/13
 */
import java.util.List;

public class MerchantUpdateBatchRequest {
	
	   /**
  * 0: 启用  1：停用  2：删除
  */
 public enum MerchantUpdateBatchRequestFlag{
     Open(0), Close(1),  Delete(2);

     int value;
     private MerchantUpdateBatchRequestFlag(int value){
         this.value = value;
     }

     public int getValue() {
         return value;
     }
 };
	
	private List<String> ids;
	private        MerchantUpdateBatchRequestFlag flag;
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public MerchantUpdateBatchRequestFlag getFlag() {
		return flag;
	}
	public void setFlag(MerchantUpdateBatchRequestFlag flag) {
		this.flag = flag;
	}
	
}
