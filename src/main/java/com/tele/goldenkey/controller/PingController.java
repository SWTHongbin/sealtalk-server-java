package com.tele.goldenkey.controller;

import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/20
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@RestController
@RequestMapping("/")
public class PingController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public APIResult<Object> ping() {
        return APIResultWrap.ok();
    }
}
