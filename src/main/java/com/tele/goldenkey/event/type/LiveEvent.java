package com.tele.goldenkey.event.type;

import com.tele.goldenkey.spi.agora.eums.RtmMsgType;
import org.springframework.context.ApplicationEvent;

public class LiveEvent extends ApplicationEvent {

    private RtmMsgType msgType;

    private Integer livedId;

    private Integer userId;


    public LiveEvent(RtmMsgType msgType, Integer livedId, Integer userId) {
        super(msgType);
        this.msgType = msgType;
        this.livedId = livedId;
        this.userId = userId;
    }

    public RtmMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(RtmMsgType msgType) {
        this.msgType = msgType;
    }

    public Integer getLivedId() {
        return livedId;
    }

    public void setLivedId(Integer livedId) {
        this.livedId = livedId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

