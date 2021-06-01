package com.tele.goldenkey.model.dto;

import com.github.pagehelper.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageDto<T> {
    /**
     * 总记录数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 本页数据
     */
    private List<T> data;


    public PageDto(Integer total, Integer totalPage, Integer pageNo, Integer pageSize, List<T> data) {
        this.total = total;
        this.totalPage = totalPage;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.data = data;
    }

    public PageDto(Integer total, List<T> data, SearchPageDto page) {
        this(total, total % page.getPageSize() == 0 ? total / page.getPageSize() : total / page.getPageSize() + 1, page.getPageNo(),
                page.getPageSize(), data);
    }

    public <R> PageDto(List<T> data, Page<R> page) {
        try {
            this.data = data;
            this.total = (int) page.getTotal();
            this.totalPage = page.getPages();
            this.pageNo = page.getPageNum();
            this.pageSize = page.getPageSize();
        } finally {
            page.close();
        }
    }

}
