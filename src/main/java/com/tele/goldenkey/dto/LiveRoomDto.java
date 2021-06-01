package com.tele.goldenkey.dto;

import com.tele.goldenkey.controller.param.LiveParam;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

@Data
public class LiveRoomDto {

    @ApiParam(name = "type", value = "语音类型")
    private Integer type;

    @ApiParam(name = "theme", value = "主题")
    private String theme;

    @ApiParam(name = "count", value = "总人数")
    private Integer count;

    @ApiParam(name = "timestamp", value = "时间戳")
    private Long timestamp;

    @ApiParam(name = "linkMai", value = "是否允许连麦")
    private Integer linkMai;

    @ApiParam(name = "anchorId", value = "主播id")
    private Integer anchorId;

    private Boolean isOpen;

    private String fmLink;

    private List<LiveParam.Goods> goods;

}
