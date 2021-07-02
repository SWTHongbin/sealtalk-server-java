package com.tele.goldenkey.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lihongbin
 * @date 2021年06月01日 11:26
 */

public class MyLiveDto {

    @Data
    public static class Rep extends SearchPageDto {
        /**
         * 直播方式
         * 1： 语音 2：视频
         */
        private Integer type;

        /**
         * 直播状态
         * 1 开播   0 关播
         */
        private Integer status;

        private List<Integer> userId;
    }

    @Data
    public static class Resp {
        /**
         * 直播id
         */
        private Long liveId;
        /**
         * 主题
         */
        private String theme;
        /**
         * 封面
         */
        private String fmLink;

        /**
         * 录制地址
         */
        private String recordUrl;
        /**
         * 用户id
         */
        private Integer userId;
        /**
         * 头像
         */
        private String portraitUri;
        /**
         * 用户名称
         */
        private String name;
    }
}
