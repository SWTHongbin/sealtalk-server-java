package com.tele.goldenkey.dto;

import lombok.Data;

@Data
public class LiveUserDto {

    private Integer userId;

    private String phone;

    /**
     * 头像地址
     */
    private String portraitUri;

    /**
     * 是否允许自己开麦  1 是 0 否
     */
    private Integer maiPower;

    /**
     * 连麦状态  1开 0 关
     */
    private Integer maiStatus;

    /**
     * 是否允许自己开语音  1 是 0 否
     */
    private Integer speakPower;

    /**
     * 语音状态  1 是 0 否
     */
    private Integer speakStatus;

    private String name;

    private Long livedId;
}
