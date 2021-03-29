package com.tele.goldenkey.live;

import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.spi.agora.eums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class KickEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.kick.name();
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) {
        userMapper.deleteByUserId(liveEventDto.getTerminalId());
        return new LiveEvent(EventType.kick, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
    }
}
