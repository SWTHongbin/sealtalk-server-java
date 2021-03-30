package com.tele.goldenkey.live;

import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.dto.LiveUserDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.service.AbstractBaseService;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiveUserService extends AbstractBaseService<LiveUser, Integer> {
    private final LiveUserMapper liveUserMapper;

    @Override
    protected Mapper<LiveUser> getMapper() {
        return liveUserMapper;
    }

    public List<LiveUserDto> getUsers(LiveUserParam param, Integer livedId) {
        LiveUser liveUser = new LiveUser();
        liveUser.setMaiStatus(param.getMaiStatus());
        liveUser.setLiveId(livedId);
        return liveUserMapper.selectByExample(liveUser).stream()
                .map(x -> {
                    LiveUserDto dto = new LiveUserDto();
                    dto.setUserId(x.getUserId());
                    dto.setPhone(x.getPhone());
                    dto.setPortraitUri(x.getPortraitUri());
                    dto.setMaiStatus(x.getMaiStatus());
                    dto.setPermissionSpeak(x.getPermissionSpeak());
                    dto.setName(x.getName());
                    dto.setLivedId(x.getLiveId());
                    return dto;
                }).collect(Collectors.toList());
    }

    public LiveUserDto getUser(Integer userId) {
        LiveUserParam param = new LiveUserParam();
        param.setUserId(userId);
        List<LiveUserDto> users = getUsers(param, null);
        if (CollectionUtils.isEmpty(users)) return null;
        return users.get(0);
    }

    public LiveEvent optionMai(Integer userId, EventType eventType) throws ServiceException {
        LiveUserDto user = getUser(userId);
        ValidateUtils.notNull(user);
        liveUserMapper.updateMai(eventType == EventType.up_mai ? 1 : 0, userId);
        return new LiveEvent(eventType, user.getLivedId(), null);
    }
}
