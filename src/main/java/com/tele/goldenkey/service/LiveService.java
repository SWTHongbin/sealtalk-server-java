package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

@Service
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {
    @Resource
    private LiveStatusesMapper liveStatusesMapper;

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    public String getPushUrl(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        return null;
    }

    public Boolean isOpen(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Integer id) {
        return liveStatusesMapper.closeByLivedId(id) > 0;
    }

    public String getLiveUrl(Integer id) {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        if (liveStatuses == null || liveStatuses.getStatus() == 0) {
            return null;
        }
        return liveStatuses.getLiveUrl();
    }

    private String createStreamUrl() {
        return null;
    }
}
