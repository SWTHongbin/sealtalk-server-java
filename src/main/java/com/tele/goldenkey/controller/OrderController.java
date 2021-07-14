package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.PrepareOrderParam;
import com.tele.goldenkey.dto.PricePackageDto;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.util.RandomUtil;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 订单相关
 */
@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController extends BaseController {

    private final RedissonClient redissonClient;

    /**
     * 预订单
     *
     * @return 订单号
     */
    @PostMapping("prepare")
    public APIResult<String> prepare(@RequestBody @Validated PrepareOrderParam orderParam) throws ServiceException {
        Integer userId = getCurrentUserId();
        String orderNo = generateNo(String.valueOf(userId) + System.currentTimeMillis());
        validateParam(orderParam.getType(), orderParam.getPackageId());
        orderParam.setUserId(userId);
        orderParam.setOrderNo(orderNo);
        redissonClient.getBucket("order" + userId + orderNo).set(orderParam, 1, TimeUnit.HOURS);
        log.info("订单号:{},订单信息:{}", orderNo, orderParam);
        return APIResultWrap.ok(orderNo);
    }

    private void validateParam(Integer type, Integer packageId) throws ServiceException {
        SkuType skuType = SkuType.byCodeOf(type);
        PricePackageDto instance = PricePackageDto.getInstance();
        ValidateUtils.isTrue(instance.findBySkuAndId(skuType, packageId).isPresent());
    }

    private String generateNo(String num) {
        return "T" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + num + RandomUtil.randomBetween(100, 999);
    }
}
