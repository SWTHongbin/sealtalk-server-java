package com.tele.goldenkey.spi.verifycode.impl;

import com.tele.goldenkey.configuration.SealtalkConfig;
import com.tele.goldenkey.constant.Constants;
import com.tele.goldenkey.constant.SmsServiceType;
import com.tele.goldenkey.domain.VerificationCodes;
import com.tele.goldenkey.spi.verifycode.BaseVerifyCodeAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/4
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Service
@Slf4j
public class DefaultVerifyCodeAuthentication extends BaseVerifyCodeAuthentication {

    @Resource
    private SealtalkConfig sealtalkConfig;

    @Override
    public SmsServiceType getIdentifier() {
        return SmsServiceType.RONGCLOUD;
    }

    /**
     * 调用融云sdk 校验短信验证码
     * @param verificationCodes
     * @param code
     */
    @Override
    protected void serviceValidate(VerificationCodes verificationCodes, String code, String env) {

        //如果是开发环境，且验证码是9999-》验证通过
        if (Constants.ENV_DEV.equals(env) && Constants.DEFAULT_VERIFY_CODE.equals(code)) {
            return;
        }
        //如果是开发环境，且短信模版没有配置-》验证通过
        if(StringUtils.isEmpty(sealtalkConfig.getRongcloudSmsRegisterTemplateId())&& Constants.DEFAULT_VERIFY_CODE.equals(code)){
            return;
        }

        //TODO，调用融云验证码校验接口，后面改为调用SDK
        return;
    }


}
