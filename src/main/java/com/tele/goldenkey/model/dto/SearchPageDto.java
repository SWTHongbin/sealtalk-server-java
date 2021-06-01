package com.tele.goldenkey.model.dto;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @date 2021年06月01日 11:23
 */
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageDto {
    /**
     * 页码 从1开始
     */
    private Integer pageNo = 1;
    /**
     * 每页大小
     */
    private Integer pageSize = 20;


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public <E> Page<E> startPage() {
        return PageHelper.startPage(this.getPageNo(), this.getPageSize());
    }
}
