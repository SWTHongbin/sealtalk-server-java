package com.tele.goldenkey.event.type;

import com.tele.goldenkey.spi.agora.eums.EventType;
import org.springframework.context.ApplicationEvent;

public class LiveEvent extends ApplicationEvent {

    private EventType msgType;

    private Integer livedId;

    private Integer terminalId;


    public LiveEvent(EventType msgType, Integer livedId, Integer terminalId) {
        super(msgType);
        this.msgType = msgType;
        this.livedId = livedId;
        this.terminalId = terminalId;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public EventType getMsgType() {
        return msgType;
    }

    public void setMsgType(EventType msgType) {
        this.msgType = msgType;
    }

    public Integer getLivedId() {
        return livedId;
    }

    public void setLivedId(Integer livedId) {
        this.livedId = livedId;
    }


}

