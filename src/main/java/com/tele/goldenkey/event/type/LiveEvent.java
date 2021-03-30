package com.tele.goldenkey.event.type;

import com.tele.goldenkey.spi.agora.eums.EventType;
import org.springframework.context.ApplicationEvent;

public class LiveEvent extends ApplicationEvent {

    private EventType msgType;

    private Integer fromUserId;

    private Integer toTerminalId;


    public LiveEvent( EventType msgType, Integer fromUserId, Integer toTerminalId) {
        super(msgType);
        this.msgType = msgType;
        this.fromUserId = fromUserId;
        this.toTerminalId = toTerminalId;
    }

    public EventType getMsgType() {
        return msgType;
    }

    public void setMsgType(EventType msgType) {
        this.msgType = msgType;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToTerminalId() {
        return toTerminalId;
    }

    public void setToTerminalId(Integer toTerminalId) {
        this.toTerminalId = toTerminalId;
    }
}

