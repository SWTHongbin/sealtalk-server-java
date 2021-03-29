package com.tele.goldenkey.live;

import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.spi.agora.eums.EventType;
import org.springframework.stereotype.Component;


@Component
public class NoSpeechEvent extends LiveEventCls {
    @Override
    String getEventName() {
        return EventType.no_speech.name();
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) {
        return new LiveEvent(EventType.no_speech, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
    }
}
