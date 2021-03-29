package com.tele.goldenkey.service;

import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.dto.LiveRoomDto;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.live.IVSClient;
import com.tele.goldenkey.util.ValidateUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ivs.model.Channel;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

@Service
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {
    @Resource
    private LiveStatusesMapper liveStatusesMapper;
    @Resource
    private IVSClient ivsClient;
    private final static String CHANNEL_KEY = "tele_tech_";

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    public String getPushUrl(Integer livedId) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null) {
            Channel channel = ivsClient.createChannel(CHANNEL_KEY + livedId);
            ValidateUtils.notNull(channel);

            liveStatuses = new LiveStatuses();
            liveStatuses.setPushUrl(channel.ingestEndpoint());
            liveStatuses.setLiveUrl(channel.playbackUrl());
            liveStatuses.setCode(channel.arn());
            liveStatuses.setLiveId(livedId);
            this.saveSelective(liveStatuses);
        } else {
            liveStatusesMapper.openById(livedId);
        }
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
        return liveStatusesMapper.closeById(livedId) > 0;
    }

    public String getLiveUrl(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }

    public void initRoom(LiveParam liveParam, Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        LiveStatuses newLive = new LiveStatuses();
        newLive.setType(liveParam.getType());
        newLive.setTheme(liveParam.getTheme());
        newLive.setCount(1);
        newLive.setLiveId(liveStatuses.getLiveId());
        liveStatusesMapper.updateByPrimaryKeySelective(newLive);
    }

    public void leave(Integer livedId, Integer num) {
        //toto
    }

    public LiveRoomDto room(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        LiveRoomDto liveRoomDto = new LiveRoomDto();
        liveRoomDto.setType(liveStatuses.getType());
        liveRoomDto.setTimestamp(System.currentTimeMillis() - liveStatuses.getStartTime().getTime());
        liveRoomDto.setTheme(liveStatuses.getTheme());
        liveRoomDto.setCount(liveStatuses.getCount());
        return liveRoomDto;
    }
}
