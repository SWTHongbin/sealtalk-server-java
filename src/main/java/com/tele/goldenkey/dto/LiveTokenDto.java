package com.tele.goldenkey.dto;

import lombok.Data;

@Data
public class LiveTokenDto {

    /**
     * 房间信息`
     */
    private LiveRoomDto roomDto;

    private String url;

    /**
     * 房间id
     */
    private Long livedId;

    /**
     *
     */
    private String rtcToken;

    /**
     *
     */
    private String channelId;

    /**
     *
     */
    private String rtmToken;

    /**
     * 用户id
     */
    private Integer userId;

    private String streamKey;
}
