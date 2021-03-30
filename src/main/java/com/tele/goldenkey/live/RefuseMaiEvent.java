package com.tele.goldenkey.live;

import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RefuseMaiEvent extends LiveEventCls {
    @Override
    String getEventName() {
        return EventType.refuse_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(terminalId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) {
        return new LiveEvent(EventType.refuse_mai, liveEventDto.getFromUserId(), liveEventDto.getToTerminalId());
    }
}
