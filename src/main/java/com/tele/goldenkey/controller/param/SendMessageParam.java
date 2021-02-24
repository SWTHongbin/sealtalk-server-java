package com.tele.goldenkey.controller.param;

import lombok.Data;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/24
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Data
public class SendMessageParam {

    private String conversationType;
    private String targetId;
    private String objectName;
    private String content;
    private String pushContent;
}
