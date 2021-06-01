package com.tele.goldenkey.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: xiuwei.nie
 * @Date: 2020/7/6
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Data
public class APIResult<T> {

    /**
     * 返回码
     */
    protected Integer code;

    /**
     * 返回码信息
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected T result;

    public APIResult(String code, String message) {
        this.code = Integer.valueOf(code);
        this.message = message;
    }

    public APIResult(String code, String message, T result) {
        this.code = Integer.valueOf(code);
        this.message = message;
        this.result = result;
    }
}
