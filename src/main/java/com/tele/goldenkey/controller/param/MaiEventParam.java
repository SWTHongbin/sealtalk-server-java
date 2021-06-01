package com.tele.goldenkey.controller.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;

@Data
public class MaiEventParam {

    /**
     * 房间id
     */
    @ApiParam(name = "livedId", value = "房间id")
    @NonNull
    private Long livedId;


    /**
     * 用户id
     */
    @ApiParam(name = "terminalId", value = "用户id")
    private Integer terminalId;

    public MaiEventParam() {
    }
}
