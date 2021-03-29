package com.tele.goldenkey.service;

import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.dto.LiveUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
                    dto.setName(x.getName());
                    return dto;
                }).collect(Collectors.toList());
    }
}
