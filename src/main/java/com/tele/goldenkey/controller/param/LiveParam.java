package com.tele.goldenkey.controller.param;

import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

@Data
public class LiveParam {

    /**
     * 直播主题
     */
    @NonNull
    private String theme;

    /**
     * 直播方式
     *
     * @see com.tele.goldenkey.enums.SkuType
     */
    @NonNull
    private Integer type;

    /**
     * 连麦  1开启  0 关闭
     */
    @NonNull
    private Integer linkMai;

    /**
     * 封面url
     */
    private String fmLink;

    /**
     * 是否开启录制
     */
    private Boolean recorde = false;

    /**
     * 商品信息
     */
    private List<GoodsParam> goods = Collections.emptyList();


    public LiveParam() {
    }
}
