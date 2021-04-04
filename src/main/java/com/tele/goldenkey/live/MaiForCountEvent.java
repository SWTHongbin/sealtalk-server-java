package com.tele.goldenkey.live;

import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.dto.LiveUserDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class MaiForCountEvent extends LiveEventCls {
    private final LiveUserMapper userMapper;

    @Override
    String getEventName() {
        return EventType.live_mai_infos.name();
    }

    @Override
    public LiveEventDto getLiveEventDto(Integer livedId, Integer userId, Integer terminalId) throws ServiceException {
        ValidateUtils.notNull(livedId);
        return new LiveEventDto(livedId, userId, terminalId);
    }

    @Override
    public LiveEvent<List<LiveUserDto>> execute(LiveEventDto liveEventDto) {
        LiveUserParam param = new LiveUserParam();
        param.setLivedId(liveEventDto.getLivedId());
        List<LiveUserDto> userDos = userMapper.selectByUserParam(param).stream()
                .map(LiveUserService::getLiveUserDto)
                .collect(Collectors.toList());
        return new LiveEvent<>(EventType.live_mai_infos, liveEventDto.getFromUserId(), liveEventDto.getToTerminalId(), userDos);
    }
}
