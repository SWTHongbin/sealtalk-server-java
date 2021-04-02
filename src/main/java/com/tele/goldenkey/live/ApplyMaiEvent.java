package com.tele.goldenkey.live;

import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ApplyMaiEvent extends LiveEventCls {

    private final LiveStatusesMapper liveStatusesMapper;

    @Override
    String getEventName() {
        return EventType.apply_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) {
        return new LiveEventDto(livedId, userId, livedId);
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(liveEventDto.getLivedId());
        ValidateUtils.notNull(liveStatuses);
        if (liveStatuses.getLinkMai() != 1) {
            throw new ServiceException(ErrorCode.SERVER_ERROR, "房间设置不允许连麦");
        }
        return new LiveEvent(EventType.apply_mai, liveEventDto.getFromUserId(), liveEventDto.getToTerminalId());
    }
}
