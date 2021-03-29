package com.tele.goldenkey.spi.agora.eums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RtmMsgType {
    join(1, "加入"),
    leave(2, "离开"),
    open(3, "开播"),
    close(4, "关播"),
    up_mai(5, "上麦"),
    down_mai(6, "下麦");

    public Integer code;

    public String dsc;
}
