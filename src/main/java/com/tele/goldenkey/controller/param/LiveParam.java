package com.tele.goldenkey.controller.param;

import lombok.Data;
import lombok.NonNull;

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
     * 1： 语音 2：视频
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
     * 商品信息
     */
    private List<Goods> goods;

    @Data
    public static class Goods {

        /**
         * 名称
         */
        private String name;

        /**
         * 商品链接
         */
        private String goodsLink;

        /**
         * 图片地址
         */
        private String pictureLink;

    }

    public LiveParam() {
    }
}
