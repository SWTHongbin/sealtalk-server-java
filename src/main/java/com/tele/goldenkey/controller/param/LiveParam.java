package com.tele.goldenkey.controller.param;

import lombok.Data;

@Data
public class LiveParam {

    /**
     * 直播主题
     */
    private String theme;

    /**
     * 直播方式
     * 1： 语音 2：视频
     */
    private Integer style;
}
