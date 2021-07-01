package com.tele.goldenkey.live;

import com.google.common.collect.Maps;
import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class ApplyMaiEvent extends LiveEventCls {

    private final LiveStatusesMapper liveStatusesMapper;
    private final LiveUserMapper userMapper;
    private final LiveService liveService;

    @Override
    String getEventName() {
        return EventType.apply_mai.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Long livedId, Integer userId, Integer terminalId) {
        return new LiveEventDto(livedId, userId, liveService.getRoomAnchorId(livedId));
    }

    @Override
    public LiveEvent<Map> execute(LiveEventDto liveEventDto) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(liveEventDto.getLivedId());
        ValidateUtils.notNull(liveStatuses);
        if (liveStatuses.getLinkMai() != 1) {
            throw new ServiceException(ErrorCode.SERVER_ERROR, "房间设置不允许连麦");
        }
        LiveUser liveUser = userMapper.selectByUserId(liveEventDto.getFromUserId());
        Map<String, String> map = Maps.newHashMap();
        map.put("name", liveUser.getName());
        map.put("portraitUri", liveUser.getPortraitUri());
        map.put("userId", liveUser.getUserId().toString());
        return new LiveEvent<>(EventType.apply_mai, liveEventDto.getLivedId(), liveEventDto.getFromUserId(), liveEventDto.getToTerminalId(), map);
    }
}
