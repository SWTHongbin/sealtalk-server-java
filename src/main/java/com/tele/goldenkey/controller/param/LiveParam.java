package com.tele.goldenkey.controller.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;

@Data
public class LiveParam {

    /**
     * 直播主题
     */
    @ApiParam(name = "theme", required = true, value = "直播主题")
    @NonNull
    private String theme;

    /**
     * 直播方式
     * 1： 语音 2：视频
     */
    @ApiParam(name = "type", required = true, value = "直播方式")
    @NonNull
    private Integer type;

    /**
     * 连麦  1开启  0 关闭
     */
    @ApiParam(name = "linkMai", required = true, value = "是否允许连麦")
    @NonNull
    private Integer linkMai;


}
