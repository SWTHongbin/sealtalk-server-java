package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
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

    public String getPushUrl(Integer id) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses == null) {
            Channel channel = ivsClient.createChannel(CHANNEL_KEY + id);
            ValidateUtils.notNull(channel);

            liveStatuses = new LiveStatuses();
            liveStatuses.setPushUrl(channel.ingestEndpoint());
            liveStatuses.setLiveUrl(channel.playbackUrl());
            liveStatuses.setCode(channel.arn());
            liveStatuses.setLiveId(id);
            this.saveSelective(liveStatuses);
        } else {
            this.isOpen(id);
        }
        return liveStatuses.getPushUrl();
    }

    public Boolean isOpen(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses != null) {
            ivsClient.stopStream(liveStatuses.getCode());
        }
        return liveStatusesMapper.closeByLivedId(id) > 0;
    }

    public String getLiveUrl(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }
}
