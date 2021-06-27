package com.tele.goldenkey.service;

import com.alibaba.fastjson.JSONObject;
import com.tele.goldenkey.controller.param.PrepareOrderParam;
import com.tele.goldenkey.dto.PricePackageDto;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.util.IosVerifyUtil;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleService {

    private final RedissonClient redissonClient;
    private final PricePackageService pricePackageService;

    public APIResult iap(Integer userId, String orderNo, String payload) throws ServiceException {
        RBucket<PrepareOrderParam> bucket = redissonClient.getBucket("order" + userId + orderNo);
        ValidateUtils.isTrue(bucket.isExists(), "订单编号不存在");
        PrepareOrderParam prepareOrderParam = bucket.get();
        SkuType skuType = SkuType.byCodeOf(prepareOrderParam.getType());
        Optional<PricePackageDto.Price> optional = PricePackageDto.getInstance().findBySkuAndId(skuType, prepareOrderParam.getPackageId());
        ValidateUtils.isTrue(optional.isPresent());

        JSONObject returnJson = getVerifyResult(payload);
        String inApp = returnJson.getString("in_app");
        List<HashMap> inApps = JSONObject.parseArray(inApp, HashMap.class);
        ValidateUtils.isTrue(!CollectionUtils.isEmpty(inApps), "未能获取获取到交易列表");
        ArrayList<String> transactionIds = new ArrayList<>();
        for (HashMap app : inApps) {
            transactionIds.add((String) app.get("transaction_id"));
        }
        //交易列表包含当前交易，则认为交易成功
        ValidateUtils.isTrue(transactionIds.contains(orderNo), "当前交易不在交易列表中");
        pricePackageService.recharge(skuType, userId, BigDecimal.valueOf(optional.get().getSecond()));
        bucket.delete();
        return APIResultWrap.ok();
    }

    private JSONObject getVerifyResult(String payload) throws ServiceException {
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
        return JSONObject.parseObject(receipt);
    }
}
