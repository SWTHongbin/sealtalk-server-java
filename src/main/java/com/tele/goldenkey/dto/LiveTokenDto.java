package com.tele.goldenkey.dto;

import lombok.Data;

@Data
public class LiveTokenDto {

    /**
     * 房间信息`
     */
    private LiveRoomDto roomDto;

    /**
     * 视频 cdn url
     */
    private String url;

    /**
     * 房间id
     */
    private Long livedId;

    /**
     * 视频 rtc token
     */
    private String rtcToken;

    /**
     * 视频 channelId
     */
    private String channelId;

    /**
     * 云信令
     */
    private String rtmToken;

    /**
     * 用户id
     */
    private Integer userId;


    private String streamKey;

    /**
     * 录制userId
     */
    private String recordeUserId;

    /**
     * 录制rtcToken
     */
    private String recordeRtcToken;
}
