package com.tele.goldenkey.controller.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LiveUserParam {

    /**
     * 1 连麦 0 未连麦
     */
    @ApiParam(name = "maiStatus", value = "连麦状态")
    private Integer maiStatus;

    private Integer userId;
}
