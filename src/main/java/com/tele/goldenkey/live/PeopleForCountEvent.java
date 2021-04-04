package com.tele.goldenkey.live;

import com.google.common.collect.Maps;
import com.tele.goldenkey.dao.LiveUserMapper;
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
public class PeopleForCountEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.live_count_people.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(livedId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Override
    public LiveEvent<Map<String, Object>> execute(LiveEventDto liveEventDto) {
        Integer count = userMapper.countByLiveId(liveEventDto.getLivedId());
        Map<String, Object> map = Maps.newHashMap();
        map.put("count", count);
        return new LiveEvent<>(EventType.live_count_people, liveEventDto.getLivedId(),liveEventDto.getFromUserId(), liveEventDto.getToTerminalId(), map);
    }
}
