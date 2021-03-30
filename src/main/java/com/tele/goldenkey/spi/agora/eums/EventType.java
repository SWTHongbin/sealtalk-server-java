package com.tele.goldenkey.spi.agora.eums;

import lombok.AllArgsConstructor;

/**
 * live 事件类型
 */
@AllArgsConstructor
public enum EventType {
    join(1, "加入", Passageway.broadcast),
    leave(2, "离开", Passageway.broadcast),
    open(3, "开播", Passageway.broadcast),
    close(4, "关播", Passageway.broadcast),
    up_mai(5, "上麦", Passageway.broadcast),
    down_mai(6, "下麦", Passageway.broadcast),
    kick(7, "T出", Passageway.broadcast),
    apply_mai(8, "申请连麦", Passageway.terminal),
    refuse_mai(9, "拒绝连麦", Passageway.terminal),
    agree_mai(10, "同意连麦", Passageway.terminal),
    no_speech(11, "禁言", Passageway.terminal);

    public Integer code;

    public String desc;
    /**
     * 消息广播类型
     */
    public Passageway passageway;

}
