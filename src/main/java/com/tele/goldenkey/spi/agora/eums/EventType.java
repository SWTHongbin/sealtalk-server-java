package com.tele.goldenkey.spi.agora.eums;

import lombok.AllArgsConstructor;

import static com.tele.goldenkey.spi.agora.eums.EventType.OptionMethod.optionMai;
import static com.tele.goldenkey.spi.agora.eums.EventType.OptionMethod.optionSpeech;

/**
 * live 事件类型
 */
@AllArgsConstructor
public enum EventType {
    join(1, "加入", Passageway.broadcast),
    leave(2, "离开", Passageway.broadcast),
    open(3, "开播", Passageway.broadcast),
    close(4, "关播", Passageway.broadcast),

    up_mai(5, "上麦", optionMai, Passageway.broadcast),
    down_mai(6, "下麦", optionMai, Passageway.broadcast),
    open_speech(13, "开语音", optionSpeech, Passageway.broadcast),
    close_speech(14, "关语音", optionSpeech, Passageway.broadcast),


    kick(7, "T出", Passageway.broadcast),
    apply_mai(8, "申请连麦", Passageway.terminal),
    refuse_mai(9, "拒绝连麦", Passageway.terminal),
    agree_mai(10, "同意连麦", Passageway.terminal),
    no_speech(11, "禁言", Passageway.terminal),
    cancel_speech(12, "取消禁言", Passageway.terminal);

    public Integer code;

    public String desc;

    public OptionMethod method;
    /**
     * 消息广播类型
     */
    public Passageway passageway;

    EventType(Integer code, String desc, Passageway passageway) {
        this.code = code;
        this.desc = desc;
        this.passageway = passageway;
    }

    public enum OptionMethod {
        optionMai, optionSpeech;
    }
}
