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
}
