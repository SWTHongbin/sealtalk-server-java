package com.tele.goldenkey.controller;

import com.tele.goldenkey.configuration.SealtalkConfig;
import com.tele.goldenkey.interceptor.ServerApiParamHolder;
import com.tele.goldenkey.model.ServerApiParams;

import javax.annotation.Resource;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/7
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
public abstract class BaseController {

    @Resource
    protected SealtalkConfig sealtalkConfig;

    protected Integer getCurrentUserId() {
        ServerApiParams serverApiParams = ServerApiParamHolder.get();
        if (serverApiParams != null) {
            return serverApiParams.getCurrentUserId();
        } else {
            return null;
        }
    }

    protected SealtalkConfig getSealtalkConfig() {
        return sealtalkConfig;
    }

    protected ServerApiParams getServerApiParams() {
        return ServerApiParamHolder.get();
    }

}
