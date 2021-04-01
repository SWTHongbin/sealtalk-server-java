package com.tele.goldenkey.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LiveUserDto {

    @ApiParam(name = "userId", value = "")
    private Integer userId;

    @ApiParam(name = "phone", value = "")
    private String phone;

    @ApiParam(name = "portraitUri", value = "头像地址")
    private String portraitUri;

    @ApiParam(name = "maiPower", value = "是否允许自己开麦  1 是 0 否")
    private Integer maiPower;

    @ApiParam(name = "maiStatus", value = "连麦状态  1开 0 关")
    private Integer maiStatus;

    @ApiParam(name = "speakPower", value = "是否允许自己开语音  1 是 0 否")
    private Integer speakPower;

    @ApiParam(name = "speakPower", value = "语音状态  1 是 0 否")
    private Integer speakStatus;

    private String name;

    private Integer livedId;
}
