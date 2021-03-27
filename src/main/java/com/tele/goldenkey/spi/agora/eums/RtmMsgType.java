package com.tele.goldenkey.spi.agora.eums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RtmMsgType {
    jion(1, "加入"),
    leave(2, "离开"),
    open(3, "开播"),
    close(4, "关播");

    public Integer code;

    public String dsc;
}
