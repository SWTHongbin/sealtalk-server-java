package com.tele.goldenkey.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LiveTokenDto {

    @ApiParam(name = "roomDto", value = "房间信息")
    private LiveRoomDto roomDto;

    private String url;

    @ApiParam(name = "livedId", value = "房间id")
    private Integer livedId;

    private String rtcToken;

    private String channelId;

    private String rtmToken;

    private Integer userId;

    private String streamKey;
}
