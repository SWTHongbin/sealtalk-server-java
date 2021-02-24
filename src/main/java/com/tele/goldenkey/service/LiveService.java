package com.tele.goldenkey.service;

import com.google.common.collect.Maps;
import com.tele.goldenkey.constant.Constants;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.manager.MiscManager;
import com.tele.goldenkey.util.N3d;
import com.tele.goldenkey.util.ValidateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {
    @Resource
    private LiveStatusesMapper liveStatusesMapper;
    @Resource
    private MiscManager miscManager;

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    @Transactional
    public HashMap<String, String> getPushUrl(Integer currentUserId, Integer id) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findByLivedId(id);
        String url = liveStatuses.getPushUrl();
        ValidateUtils.notEmpty(url);
        miscManager.sendMessage(currentUserId, Constants.CONVERSATION_TYPE_GROUP, id, null, null, null, N3d.encode(id));
        liveStatusesMapper.openByLivedId(id);
        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("pushUrl", url);
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

}
