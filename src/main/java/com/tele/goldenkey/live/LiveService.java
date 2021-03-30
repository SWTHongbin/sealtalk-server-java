package com.tele.goldenkey.live;

import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dao.UsersMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.domain.Users;
import com.tele.goldenkey.dto.LiveRoomDto;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.service.AbstractBaseService;
import com.tele.goldenkey.spi.live.IVSClient;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.ivs.model.Channel;
import tk.mybatis.mapper.common.Mapper;

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

    public String getPushUrl(Integer livedId) throws ServiceException {
        liveUserMapper.deleteByLivedId(livedId);
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);

        if (liveStatuses != null) {
            liveStatusesMapper.openById(livedId);
        } else {
            liveStatuses = buildLiveStatus(livedId);
            ValidateUtils.notNull(liveStatuses);
            this.saveSelective(liveStatuses);
        }
        LiveUser liveUser = convertLiveUser(getUserById(livedId), livedId);
        liveUser.setMaiPower(1);
        liveUserMapper.insertSelective(liveUser);
        return liveStatuses.getPushUrl();
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
        liveUserMapper.deleteByLivedId(livedId);
        return true;
    }

    @Transactional
    public String getLiveUrl(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }

    public void initRoom(LiveParam liveParam, Integer livedId) {
        boolean flag = liveStatusesMapper.findById(livedId) == null;
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setLiveId(livedId);
        liveStatuses.setType(liveParam.getType());
        liveStatuses.setTheme(liveParam.getTheme());
        liveStatuses.setLinkMai(liveParam.getLinkMai());
        if (flag) {
            liveStatusesMapper.insertSelective(liveStatuses);
        } else {
            liveStatusesMapper.updateByPrimaryKeySelective(liveStatuses);
        }
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
        LiveRoomDto liveRoomDto = new LiveRoomDto();
        liveRoomDto.setType(liveStatuses.getType());
        if (liveStatuses.getStartTime() != null) {
            liveRoomDto.setTimestamp(System.currentTimeMillis() - liveStatuses.getStartTime().getTime());
        }
        liveRoomDto.setTheme(liveStatuses.getTheme());
        liveRoomDto.setLinkMai(liveStatuses.getLinkMai());
        liveRoomDto.setCount(liveUserMapper.countByLiveId(livedId));
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
        Channel channel = ivsClient.createChannel(CHANNEL_KEY + livedId);
        if (channel == null) return null;
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setPushUrl(channel.ingestEndpoint());
        liveStatuses.setLiveUrl(channel.playbackUrl());
        liveStatuses.setCode(channel.arn());
        liveStatuses.setLiveId(livedId);
        return liveStatuses;
    }
}
