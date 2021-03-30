package com.tele.goldenkey.live;

import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownMaiEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.down_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) {
        return null;
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) throws ServiceException {
        userMapper.updateMai(0, liveEventDto.getTerminalId());
        return new LiveEvent(EventType.down_mai, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
    }
}
