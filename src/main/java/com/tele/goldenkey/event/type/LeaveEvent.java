package com.tele.goldenkey.event.type;

import org.springframework.context.ApplicationEvent;

public class LeaveEvent extends ApplicationEvent {

    private Integer currentUserId;

    private Integer targetId;

    public LeaveEvent(Integer currentUserId, Integer targetId) {
        super(currentUserId);
        this.currentUserId = currentUserId;
        this.targetId = targetId;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }


}

