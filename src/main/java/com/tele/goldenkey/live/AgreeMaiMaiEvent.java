package com.tele.goldenkey.live;

import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class AgreeMaiMaiEvent extends LiveEventCls {
    private final LiveUserMapper liveUserMapper;

    @Override
    String getEventName() {
        return EventType.agree_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(terminalId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Transactional
    @Override
    public LiveEvent<Void> execute(LiveEventDto liveEventDto) {
        liveUserMapper.openMaiPower(liveEventDto.getToTerminalId());
        return new LiveEvent<>(EventType.agree_mai, liveEventDto.getLivedId(), liveEventDto.getFromUserId(), liveEventDto.getToTerminalId());
    }
}
