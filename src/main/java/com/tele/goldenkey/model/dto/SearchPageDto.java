package com.tele.goldenkey.model.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @date 2021年06月01日 11:23
 */
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageDto {
    /**
     * 页码
     */
    private Long pageNo = 1L;
    /**
     * 每页大小
     */
    private Long pageSize = 20L;

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
