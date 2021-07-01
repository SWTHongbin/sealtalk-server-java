package com.tele.goldenkey.spi.agora.eums;

import lombok.AllArgsConstructor;

import static com.tele.goldenkey.spi.agora.eums.EventType.PersonalFunction.optionMai;
import static com.tele.goldenkey.spi.agora.eums.EventType.PersonalFunction.optionSpeech;

/**
 * live 事件类型
 */
@AllArgsConstructor
public enum EventType {

    live_count_people(15, "人数统计", null, null, Passageway.broadcast),
    live_mai_infos(16, "麦位列表信息", null, null, Passageway.broadcast),

    //个人事件
    join(1, "加入", new EventType[]{live_count_people}, null, Passageway.broadcast),
    leave(2, "离开", new EventType[]{live_count_people}, null, Passageway.broadcast),
    open(3, "开播", null, null, Passageway.broadcast),
    close(4, "关播", null, null, Passageway.broadcast),
    up_mai(5, "上麦", new EventType[]{live_mai_infos}, optionMai, Passageway.broadcast),
    down_mai(6, "下麦", new EventType[]{live_mai_infos}, optionMai, Passageway.broadcast),
    open_speech(13, "开语音", new EventType[]{live_mai_infos}, optionSpeech, Passageway.broadcast),
    close_speech(14, "关语音", new EventType[]{live_mai_infos}, optionSpeech, Passageway.broadcast),

    // 群事件
    kick(7, "T出", new EventType[]{live_count_people, live_mai_infos}, null, Passageway.broadcast),
    apply_mai(8, "申请连麦", null, null, Passageway.terminal),
    refuse_mai(9, "拒绝连麦", null, null, Passageway.terminal),
    agree_mai(10, "同意连麦", null, null, Passageway.terminal),
    no_speech(11, "禁言", null, null, Passageway.terminal),
    cancel_speech(12, "取消禁言", null, null, Passageway.terminal);

    public Integer code;

    public String desc;

    /**
     * 额外事件
     */
    public EventType[] additionalEvent;

    /**
     * 个人
     */
    public PersonalFunction personalFunction;
    /**
     * 消息广播类型
     */
    public Passageway passageway;


    /**
     * 个人事件功能
     */
    public enum PersonalFunction {
        optionMai, optionSpeech;
    }
}
