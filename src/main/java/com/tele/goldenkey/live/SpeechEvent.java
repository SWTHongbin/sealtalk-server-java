package com.tele.goldenkey.live;

import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SpeechEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.cancel_speech.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(terminalId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Override
    public LiveEvent<Void> execute(LiveEventDto liveEventDto) {
        userMapper.openSpeakPower(liveEventDto.getToTerminalId());
        return new LiveEvent<>(EventType.cancel_speech,liveEventDto.getLivedId(), liveEventDto.getFromUserId(), liveEventDto.getToTerminalId());
    }
}
