package com.tele.goldenkey.controller.param;

import lombok.Data;

@Data
public class LiveUserParam {

    /**
     * 1 连麦 0 未连麦
     */
    private Integer maiStatus;

    private Integer userId;

    private Long livedId;

    public LiveUserParam() {
    }
}
