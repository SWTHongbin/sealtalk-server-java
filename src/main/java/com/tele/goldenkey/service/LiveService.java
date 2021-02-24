package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.live.LiveSpiService;
import com.tele.goldenkey.util.N3d;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

@Service
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {
    @Resource
    private LiveStatusesMapper liveStatusesMapper;
    @Resource
    private LiveSpiService liveSpiService;

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    public String getPushUrl(Integer id) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses == null) {
            liveStatuses = new LiveStatuses();
            liveStatuses.setLiveId(id);
            this.saveSelective(liveStatuses);
        } else {
            liveStatusesMapper.openByLivedId(id);
        }
        return liveSpiService.pushUrl(N3d.encode(id));
    }

    public Boolean isOpen(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Integer id) {
        return liveStatusesMapper.closeByLivedId(id) > 0;
    }

    public String getLiveUrl(Integer id) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveSpiService.liveUrl(N3d.encode(id));
    }

}
