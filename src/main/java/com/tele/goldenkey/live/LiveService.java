package com.tele.goldenkey.live;

import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dao.UsersMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.domain.Users;
import com.tele.goldenkey.dto.LiveRoomDto;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.service.AbstractBaseService;
import com.tele.goldenkey.spi.live.IVSClient;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ivs.model.CreateChannelResponse;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {
    private final static String CHANNEL_KEY = "tele_tech_";

    private final LiveStatusesMapper liveStatusesMapper;
    private final IVSClient ivsClient;
    private final LiveUserMapper liveUserMapper;
    private final UsersMapper usersMapper;

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }


    public LiveTokenDto anchor(Integer livedId) throws ServiceException {
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (StringUtils.isBlank(liveStatuses.getPushUrl())) {
            liveStatuses = buildLiveStatus(livedId);
            ValidateUtils.notNull(liveStatuses);
            this.updateByPrimaryKeySelective(liveStatuses);
        }
        liveTokenDto.setUrl(liveStatuses.getPushUrl());
        liveTokenDto.setRoomDto(room(livedId));
        liveTokenDto.setLivedId(livedId);
        liveTokenDto.setStreamKey(liveStatuses.getStreamKey());
        liveTokenDto.setUserId(livedId);
        return liveTokenDto;
    }

    public Boolean isOpen(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses != null) {
            ivsClient.stopStream(liveStatuses.getCode());
        }
        liveStatusesMapper.closeById(livedId);
        return true;
    }

    public String getLiveUrl(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }

    public void initRoom(LiveParam liveParam, Integer livedId) {
        liveUserMapper.deleteByLivedId(livedId);
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setLiveId(livedId);
        liveStatuses.setType(liveParam.getType());
        liveStatuses.setTheme(liveParam.getTheme());
        liveStatuses.setStartTime(new Date());
        liveStatuses.setStatus(1);
        liveStatuses.setLinkMai(liveParam.getLinkMai());
        if (liveStatusesMapper.findById(livedId) == null) {
            liveStatusesMapper.insertSelective(liveStatuses);
        } else {
            liveStatusesMapper.updateByPrimaryKeySelective(liveStatuses);
        }
        LiveUser liveUser = convertLiveUser(getUserById(livedId), livedId);
        liveUser.setMaiPower(1);
        liveUser.setMaiStatus(1);
        liveUserMapper.insertSelective(liveUser);
    }

    public Integer leave(Integer userId) throws ServiceException {
        LiveUser liveUser = liveUserMapper.selectByUserId(userId);
        ValidateUtils.notNull(liveUser);
        liveUserMapper.deleteByUserId(userId);
        return liveUser.getLiveId();
    }

    public void join(Integer userId, Integer livedId) {
        liveUserMapper.deleteByUserId(userId);
        liveUserMapper.insertSelective(convertLiveUser(getUserById(userId), livedId));
    }

    public LiveRoomDto room(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null) return null;
        LiveRoomDto liveRoomDto = new LiveRoomDto();
        liveRoomDto.setType(liveStatuses.getType());
        liveRoomDto.setIsOpen(liveStatuses.getStatus() == 1);
        long timestamp = liveRoomDto.getIsOpen() ? liveStatuses.getStartTime().getTime() : liveStatuses.getUpdatedAt().getTime() - liveStatuses.getStartTime().getTime();
        liveRoomDto.setTimestamp(timestamp);
        liveRoomDto.setTheme(liveStatuses.getTheme());
        liveRoomDto.setLinkMai(liveStatuses.getLinkMai());
        liveRoomDto.setCount(liveUserMapper.countByLiveId(livedId));
        liveRoomDto.setAnchorId(livedId);
        return liveRoomDto;
    }

    private Users getUserById(Integer id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    private LiveUser convertLiveUser(Users users, Integer livedId) {
        LiveUser liveUser = new LiveUser();
        liveUser.setUserId(users.getId());
        liveUser.setPhone(users.getPhone());
        liveUser.setLiveId(livedId);
        liveUser.setName(users.getNickname());
        liveUser.setPortraitUri(users.getPortraitUri());
        return liveUser;
    }

    private LiveStatuses buildLiveStatus(Integer livedId) {
        CreateChannelResponse createChannel = ivsClient.createChannel(CHANNEL_KEY + livedId);
        if (createChannel == null) return null;
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setPushUrl(createChannel.channel().ingestEndpoint());
        liveStatuses.setLiveUrl(createChannel.channel().playbackUrl());
        liveStatuses.setCode(createChannel.channel().arn());
        liveStatuses.setLiveId(livedId);
        liveStatuses.setStreamKey(createChannel.streamKey().value());
        return liveStatuses;
    }


}
