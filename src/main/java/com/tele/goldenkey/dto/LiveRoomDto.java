package com.tele.goldenkey.dto;

import com.tele.goldenkey.domain.Goods;
import lombok.Data;

import java.util.List;

@Data
public class LiveRoomDto {

    /**
     * 语音类型语音类型
     */
    private Integer type;

    /**
     * 主题
     */
    private String theme;

    /**
     * 总人数
     */
    private Integer count;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 是否允许连麦
     */
    private Integer linkMai;

    /**
     * 录制地址
     */
    private String recordUrl;

    /**
     * 主播id
     */
    private Integer anchorId;

    private Boolean isOpen;

    /**
     * 封面url
     */
    private String fmLink;

    private List<Goods> goods;

}
