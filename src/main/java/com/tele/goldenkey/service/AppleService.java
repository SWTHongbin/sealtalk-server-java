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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleService {

    private final RedissonClient redissonClient;
    private final PricePackageService pricePackageService;

    @Transactional
    public APIResult iap(Integer userId, String orderNo, String payload) throws ServiceException {
        RLock lock = redissonClient.getLock("order.lock." + orderNo);
        ValidateUtils.isTrue(lock.tryLock(), "请稍等");
        try {
            RBucket<PrepareOrderParam> bucket = redissonClient.getBucket("order" + userId + orderNo);
            ValidateUtils.isTrue(bucket.isExists(), "订单编号不存在");
            PrepareOrderParam prepareOrderParam = bucket.get();
            SkuType skuType = SkuType.byCodeOf(prepareOrderParam.getType());
            Optional<PricePackageDto.Price> optional = PricePackageDto.getInstance().findBySkuAndId(skuType, prepareOrderParam.getPackageId());
            ValidateUtils.isTrue(optional.isPresent());
            verifyResult(payload);
            log.info("订单号:{},userId:{},苹果支付回调:{}", orderNo, userId, prepareOrderParam);
            pricePackageService.recharge(skuType, userId, BigDecimal.valueOf(optional.get().getSecond()));
            bucket.delete();
            return APIResultWrap.ok();
        } finally {
            lock.unlock();
        }
    }

    private void verifyResult(String payload) throws ServiceException {
        String verifyResult = IosVerifyUtil.buyAppVerify(payload, 1);
        ValidateUtils.notNull(verifyResult, "苹果验证失败,返回数据为空");
        log.info("线上，苹果平台返回JSON:" + verifyResult);
        JSONObject appleReturn = JSONObject.parseObject(verifyResult);
        String states = appleReturn.getString("status");
        if (Objects.equals("21007", states)) {
            verifyResult = IosVerifyUtil.buyAppVerify(payload, 0);
            log.info("沙盒环境，苹果平台返回JSON:" + verifyResult);
            appleReturn = JSONObject.parseObject(verifyResult);
            states = appleReturn.getString("status");
        }
        log.info("苹果平台返回值：appleReturn" + appleReturn);
        ValidateUtils.isTrue(Objects.equals("0", states), "支付失败,错误码：" + states);
    }
}
