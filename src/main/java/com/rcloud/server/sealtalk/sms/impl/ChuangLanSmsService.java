package com.rcloud.server.sealtalk.sms.impl;

import com.alibaba.fastjson.JSON;
import com.rcloud.server.sealtalk.configuration.SealtalkConfig;
import com.rcloud.server.sealtalk.constant.ErrorCode;
import com.rcloud.server.sealtalk.constant.SmsServiceType;
import com.rcloud.server.sealtalk.exception.ServiceException;
import com.rcloud.server.sealtalk.sms.SmsService;
import com.rcloud.server.sealtalk.sms.dto.SmsSendRequest;
import com.rcloud.server.sealtalk.sms.dto.SmsSendResponse;
import com.rcloud.server.sealtalk.util.ChuangLanSmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class ChuangLanSmsService implements SmsService {
    @Resource
    protected SealtalkConfig sealtalkConfig;

    @Override
    public SmsServiceType getIdentifier() {
        return SmsServiceType.CHUANGLAN;
    }

    /**
     * 发送验证码
     *
     * @param region 地区，如中国大陆地区 region=86
     * @param phone  手机号
     * @return 返回验证码对应的唯一标示
     * @throws ServiceException
     */
    @Override
    public String sendVerificationCode(String region, String phone) throws ServiceException {
        String code = String.valueOf((int) ((Math.random() * 9) * 1000));
        boolean flag;
        if (region.startsWith("86") || region.startsWith("0086")) {
            flag = sendSmsByChuanglanToChina(phone, code);
        } else {
            flag = sendSmsByChuanglanToAbroad(phone, code);
        }
        if (flag) return code;
        throw new ServiceException(ErrorCode.CHUANGLAN_SMS_FAILD);
    }

    private static String CHINA_ACCOUNT = "YZM2064149";
    private static String CHINA_PSWD = "rDza791ps";
    private static String CHINA_MSG_URL = "http://smssh1.253.com/msg/send/json";

    /**
     * 创蓝国内
     *
     * @param mobile
     * @param validateNum
     * @return
     */
    private boolean sendSmsByChuanglanToChina(String mobile, String validateNum) {
        String msg = String.format("【Telepathy】您的验证码是%s。请不要泄露你的密码", validateNum);
        SmsSendRequest smsSingleRequest = new SmsSendRequest(CHINA_ACCOUNT, CHINA_PSWD, msg, mobile, "true");
        String requestJson = JSON.toJSONString(smsSingleRequest);
        log.info("before request string is: " + requestJson);
        String response = ChuangLanSmsUtil.sendSmsByPost(CHINA_MSG_URL, requestJson);
        log.info("response after request result is :" + response);
        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        if (smsSingleResponse == null) return false;
        return "0".equals(smsSingleResponse.getCode());
    }

    private static String ABROAD_ACCOUNT = "I0433601";
    private static String ABROAD_PSWD = "7GIzpaO2jqcd87";
    private static String ABROAD_MSG_URL = "http://intapi.253.com/send/json";

    /**
     * 创蓝国外
     *
     * @param mobile
     * @param validateNum
     * @return
     */
    private boolean sendSmsByChuanglanToAbroad(String mobile, String validateNum) {
        String msg = String.format("[Telestudio]Your verification code is %s.", validateNum);
        SmsSendRequest smsSingleRequest = new SmsSendRequest(ABROAD_ACCOUNT, ABROAD_PSWD, msg, "00".concat(mobile));
        String requestJson = JSON.toJSONString(smsSingleRequest);
        log.info("before abroad request string is: " + requestJson);
        String response = ChuangLanSmsUtil.sendSmsByPost(ABROAD_MSG_URL, requestJson);
        log.info("response abroad after request result is :" + response);
        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        if (smsSingleResponse == null) return false;
        return "0".equals(smsSingleResponse.getCode());
    }
}
