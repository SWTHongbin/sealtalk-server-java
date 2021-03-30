package com.tele.goldenkey.live;

import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
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
public class UpMaiEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;
    private final LiveStatusesMapper statusesMapper;

    @Override
    String getEventName() {
        return EventType.up_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) {
        return null;
    }

    @Override
    public LiveEvent execute(LiveEventDto liveEventDto) throws ServiceException {
        LiveStatuses liveStatuses = statusesMapper.findById(liveEventDto.getLivedId());
        ValidateUtils.notNull(liveStatuses);
        if (liveStatuses.getLinkMai() != 1) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "不允许开麦");
        }
        userMapper.updateMai(1, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
        return new LiveEvent(EventType.up_mai, liveEventDto.getLivedId(), liveEventDto.getTerminalId());
    }
}
