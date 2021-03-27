package com.tele.goldenkey.service;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.live.IVSClient;
import com.tele.goldenkey.util.ValidateUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ivs.model.Channel;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import java.util.Map;

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
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
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
            liveStatusesMapper.openByLivedId(livedId);
        }
        return liveStatuses.getPushUrl();
    }

    public Boolean isOpen(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
        if (liveStatuses != null) {
            ivsClient.stopStream(liveStatuses.getCode());
        }
        return liveStatusesMapper.closeByLivedId(livedId) > 0;
    }

    public String getLiveUrl(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }

    public void initRoom(LiveParam liveParam, Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
        LiveStatuses newLive = new LiveStatuses();
        newLive.setStyle(liveParam.getStyle());
        newLive.setTheme(liveParam.getTheme());
        newLive.setCount(1);
        newLive.setId(liveStatuses.getId());
        liveStatusesMapper.updateByPrimaryKeySelective(newLive);
    }

    public void leave(Integer livedId, Integer num) {
        liveStatusesMapper.updateCount(livedId, num);
    }

    public Map<String, Object> room(Integer livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(livedId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("style", liveStatuses.getStyle());
        map.put("theme", liveStatuses.getTheme());
        map.put("count", liveStatuses.getCount());
        map.put("timestamp", System.currentTimeMillis() - liveStatuses.getStartTime().getTime());
        return map;
    }
}
