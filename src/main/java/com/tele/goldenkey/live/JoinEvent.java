package com.tele.goldenkey.live;

import com.google.common.collect.Maps;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JoinEvent extends LiveEventCls {

    private final LiveUserMapper liveUserMapper;

    @Override
    String getEventName() {
        return EventType.join.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        return null;
    }


    @Transactional
    @Override
    public LiveEvent<Map> execute(LiveEventDto liveEventDto) {
        LiveUser liveUser = liveUserMapper.selectByUserId(liveEventDto.getFromUserId());
        Map<String, Object> map = Maps.newHashMap();
        map.put("userId", liveUser.getUserId());
        map.put("name", liveUser.getName());
        return new LiveEvent<>(EventType.join, liveEventDto.getLivedId(), null, null, map);
    }
}
