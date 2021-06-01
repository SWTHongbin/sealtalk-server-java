package com.tele.goldenkey.controller.param;

import lombok.Data;
import lombok.NonNull;

@Data
public class MaiEventParam {

    /**
     * 房间id
     */
    @NonNull
    private Long livedId;


    /**
     * 用户id
     */
    private Integer terminalId;

    public MaiEventParam() {
    }
}
