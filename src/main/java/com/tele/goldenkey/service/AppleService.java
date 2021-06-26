package com.tele.goldenkey.service;

import com.alibaba.fastjson.JSONObject;
import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.util.IosVerifyUtil;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class AppleService {

    public APIResult iap(String transactionId, String payload) throws ServiceException {
        String verifyResult = IosVerifyUtil.buyAppVerify(payload, 1);
        ValidateUtils.notNull(verifyResult, "苹果验证失败,返回数据为空");
        log.info("线上，苹果平台返回JSON:" + verifyResult);
        JSONObject appleReturn = JSONObject.parseObject(verifyResult);
        String states = appleReturn.getString("status");
        //无数据则沙箱环境验证
        if ("21007".equals(states)) {
            verifyResult = IosVerifyUtil.buyAppVerify(payload, 0);
            log.info("沙盒环境，苹果平台返回JSON:" + verifyResult);
            appleReturn = JSONObject.parseObject(verifyResult);
            states = appleReturn.getString("status");
        }
        log.info("苹果平台返回值：appleReturn" + appleReturn);
        ValidateUtils.isTrue(states.equals("0"), "支付失败，错误码：" + states);
        String receipt = appleReturn.getString("receipt");
        JSONObject returnJson = JSONObject.parseObject(receipt);
        String inApp = returnJson.getString("in_app");
        List<HashMap> inApps = JSONObject.parseArray(inApp, HashMap.class);
        if (!CollectionUtils.isEmpty(inApps)) {
            ArrayList<String> transactionIds = new ArrayList<>();
            for (HashMap app : inApps) {
                transactionIds.add((String) app.get("transaction_id"));
            }
            //交易列表包含当前交易，则认为交易成功
            if (transactionIds.contains(transactionId)) {
                return APIResultWrap.ok();
            }
            return APIResultWrap.error(ErrorCode.PARAM_ERROR.getErrorCode(), "当前交易不在交易列表中");
        }
        return APIResultWrap.error(ErrorCode.PARAM_ERROR.getErrorCode(), "未能获取获取到交易列表");
    }
}
