package com.tele.goldenkey.live;

import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;

/**
 * 事件类型
 */
public abstract class LiveEventCls {

    abstract String getEventName();

    public abstract LiveEventDto getLiveEventDto(Long livedId, Integer userId, Integer terminalId) throws ServiceException;

    public abstract LiveEvent execute(LiveEventDto liveEventDto) throws ServiceException;
}
