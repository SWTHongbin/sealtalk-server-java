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
    @ApiParam(name = "maiStatus", value = "连麦状态")
    private Integer maiStatus;
    /**
     *
     */
    @ApiParam(name = "permissionSpeak", value = "是否允许发言")
    private Integer permissionSpeak;

    private String name;
}
