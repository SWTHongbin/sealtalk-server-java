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
 * apple  相关
 */

@RestController
@RequestMapping("/apple")
@RequiredArgsConstructor
public class AppleController extends BaseController {

    private final AppleService appleService;

    /**
     * 苹果内购校验
     *
     * @param transactionId 苹果内购交易ID
     * @param payload       校验体（base64字符串）
     * @return
     */
    @GetMapping("/iap")
    public APIResult iap(@RequestParam("transactionId") String transactionId, @RequestParam("payload") String payload) throws ServiceException {
        return appleService.iap(transactionId, payload);
    }
}
