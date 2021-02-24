package com.tele.goldenkey.rongcloud;


import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.interceptor.ServerApiParamHolder;
import io.rong.models.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/6
 * @Description: 融云服务调用模版
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Slf4j
public class RongCloudInvokeTemplate {

    public static <T extends Result> T getData(RongCloudCallBack<T> action) throws ServiceException {

        try {
            Result result = action.doInvoker();
            if (result.getCode().equals(200)) {
                return (T) result;
            } else {
                log.error("invoke rongcloud server exception,resultCode={},errorMessage={},traceId={}", result.getCode(), result.getErrorMessage(), ServerApiParamHolder.getTraceId());
                return (T) result;
            }
        } catch (Exception e) {
            log.error("invoke rongcloud server error：" + e.getMessage() + " ,traceId=" + ServerApiParamHolder.getTraceId(), e);
            throw new ServiceException(ErrorCode.INVOKE_RONG_CLOUD_SERVER_ERROR);
        }
    }
}
