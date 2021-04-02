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
public class KickEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.kick.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(terminalId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Override
    public LiveEvent<Void> execute(LiveEventDto liveEventDto) {
        userMapper.deleteByUserId(liveEventDto.getToTerminalId());
        return new LiveEvent<>(EventType.kick, liveEventDto.getLivedId(), null);
    }
}
