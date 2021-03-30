package com.tele.goldenkey.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LiveUserDto {
    /**
     *
     */
    @ApiParam(name = "userId", value = "")
    private Integer userId;
    /**
     *
     */
    @ApiParam(name = "phone", value = "")
    private String phone;
    /**
     *
     */
    @ApiParam(name = "portraitUri", value = "")
    private String portraitUri;

    /**
     *
     */
    @ApiParam(name = "maiStatus", value = "连麦状态  1开 0 关")
    private Integer maiStatus;
    /**
     *
     */
    @ApiParam(name = "permissionSpeak", value = "是否允许发言  1是 0 否")
    private Integer permissionSpeak;

    @ApiParam(name = "maiPower", value = "是否允许自己开麦  1 是 0 否")
    private Integer maiPower;

    private String name;

    private Integer livedId;
}
