package com.tele.goldenkey.live;

import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveStatuses;
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
    private final LiveStatusesMapper statusesMapper;

    @Override
    protected Mapper<LiveUser> getMapper() {
        return liveUserMapper;
    }

    public List<LiveUserDto> getUsers(LiveUserParam param) {
        return liveUserMapper.selectByUserParam(param).stream()
                .map(this::getLiveUserDto).collect(Collectors.toList());
    }


    public LiveUserDto getUser(Integer userId) {
        LiveUserParam param = new LiveUserParam();
        param.setUserId(userId);
        List<LiveUserDto> users = getUsers(param);
        if (CollectionUtils.isEmpty(users)) return null;
        return users.get(0);
    }

    public LiveEvent optionMai(Integer userId, EventType eventType) throws ServiceException {
        LiveUserDto user = getUser(userId);
        ValidateUtils.notNull(user);
        int code = 0;
        if (eventType == EventType.up_mai) {
            LiveStatuses liveStatuses = statusesMapper.findById(user.getLivedId());
            ValidateUtils.notNull(liveStatuses);
            if (liveStatuses.getLinkMai() != 1) {
                throw new ServiceException(ErrorCode.PARAM_ERROR, "不允许开麦");
            }
            if (user.getMaiPower() != 1) {
                throw new ServiceException(ErrorCode.PARAM_ERROR, "请先申请连麦");
            }
        }
        liveUserMapper.updateMai(code, userId);
        return new LiveEvent(eventType, user.getLivedId(), null);
    }

    private LiveUserDto getLiveUserDto(LiveUser x) {
        LiveUserDto dto = new LiveUserDto();
        dto.setUserId(x.getUserId());
        dto.setPhone(x.getPhone());
        dto.setPortraitUri(x.getPortraitUri());
        dto.setMaiStatus(x.getMaiStatus());
        dto.setPermissionSpeak(x.getPermissionSpeak());
        dto.setName(x.getName());
        dto.setMaiPower(x.getMaiPower());
        dto.setLivedId(x.getLiveId());
        return dto;
    }
}
