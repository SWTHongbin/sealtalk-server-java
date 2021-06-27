package com.tele.goldenkey.controller;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.service.AppleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付回调  相关
 */

@RestController
@RequestMapping("pay")
@RequiredArgsConstructor
public class PayController extends BaseController {

    private final AppleService appleService;

    /**
     * 苹果内购校验
     *
     * @param orderNo 订单号
     * @param payload 校验体（base64字符串）
     * @return
     */
    @GetMapping("/ios/iap")
    public APIResult iap(@RequestParam("orderNo") String orderNo, @RequestParam("payload") String payload) throws ServiceException {
        return appleService.iap(getCurrentUserId(), orderNo, payload);
    }
}
