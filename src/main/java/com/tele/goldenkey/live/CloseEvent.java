package com.tele.goldenkey.live;

import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.dto.LiveRoomDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CloseEvent extends LiveEventCls {
    private final LiveService liveService;

    @Override
    String getEventName() {
        return EventType.close.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Long livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(livedId);
        return new LiveEventDto(livedId, null, null);
    }

    @Override
    public LiveEvent<LiveRoomDto> execute(LiveEventDto liveEventDto) {
        LiveRoomDto room = liveService.room(liveEventDto.getLivedId(), liveEventDto.getToTerminalId());
        return new LiveEvent<>(EventType.close, liveEventDto.getLivedId(), null, null, room);
    }
}
