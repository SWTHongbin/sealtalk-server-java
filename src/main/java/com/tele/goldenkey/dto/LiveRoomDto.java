package com.tele.goldenkey.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

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
}
