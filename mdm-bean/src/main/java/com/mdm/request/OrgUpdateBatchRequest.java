/** * 
@author  wanglei   E-mail  ahuiwanglei@126.com 
@date 创建时间：2016年11月2日 下午2:50:15 *
**/ 
package com.mdm.request;

import java.util.List;

public class OrgUpdateBatchRequest {
	
	   /**
     * 0: 启用  1：停用  2：删除
     */
    public enum OrgUpdateBatchRequestFlag{
        Open(0), Close(1),  Delete(2);

        int value;
        private OrgUpdateBatchRequestFlag(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };
	
	private List<String> ids;
	private        OrgUpdateBatchRequestFlag flag;
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public OrgUpdateBatchRequestFlag getFlag() {
		return flag;
	}
	public void setFlag(OrgUpdateBatchRequestFlag flag) {
		this.flag = flag;
	}
	
}
