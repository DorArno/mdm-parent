package com.mdm.common;

import java.util.List;

/**
 * @version 创建时间：2016年9月13日 下午9:28:38
 *          分页返回结果bean
 * @author:gaoyb
 */

public class PageResultBean {

    private List<?> list;

    private long totalCount;

    private long maxPk;

    private long minPk;

    /**
     * @return the list
     */
    public List<?> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<?> list) {
        this.list = list;
    }

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount == null ? 0 : totalCount.longValue();
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount == null ? 0 : totalCount.longValue();
    }

    public long getMaxPk() {
        return maxPk;
    }

    public void setMaxPk(Long maxPk) {
        this.maxPk = maxPk == null ? 0 : maxPk.longValue();
    }

    public long getMinPk() {
        return minPk;
    }

    public void setMinPk(Long minPk) {
        this.minPk = minPk == null ? 0 : minPk.longValue();

    }
}
