package com.tele.goldenkey.live;

import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.spi.agora.eums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AgreeMaiMaiEvent extends LiveEventCls {
    private final LiveUserMapper liveUserMapper;

    @Override
    String getEventName() {
        return EventType.agree_mai.name();
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) {
        liveUserMapper.updateMai(1, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
        return new LiveEvent(EventType.agree_mai, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
    }
}
