package com.tele.goldenkey.event;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tele.goldenkey.constant.Constants;
import com.tele.goldenkey.event.type.LeaveEvent;
import com.tele.goldenkey.event.type.OpenLiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.manager.MiscManager;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.service.UsersService;
import com.tele.goldenkey.util.N3d;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
public class LiveListener {
    @Resource
    private MiscManager miscManager;
    @Resource
    private UsersService usersService;
    @Resource
    private LiveService liveService;

    @Async
    @EventListener
    public void openLive(OpenLiveEvent event) throws ServiceException {
        String liveUrl = liveService.getLiveUrl(event.getTargetId());
        if (StringUtils.isBlank(liveUrl)) return;

        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("username", usersService.getCurrentUserNickNameWithCache(event.getCurrentUserId()));
        hashMap.put("liveUrl", liveUrl);
        String jsonStr = JSON.toJSONString(hashMap);
        miscManager.sendMessage(event.getCurrentUserId(),
                Constants.CONVERSATION_TYPE_GROUP,
                event.getTargetId(),
                "1",
                jsonStr,
                jsonStr,
                N3d.encode(event.getTargetId()));
    }

    @Async
    @EventListener
    public void leave(LeaveEvent event) throws ServiceException {
        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("username", usersService.getCurrentUserNickNameWithCache(event.getCurrentUserId()));
        hashMap.put("message", "离开房间");
        hashMap.put("code", "3");
        String jsonStr = JSON.toJSONString(hashMap);
        miscManager.sendMessage(event.getCurrentUserId(),
                Constants.CONVERSATION_TYPE_GROUP,
                event.getTargetId(),
                "1",
                jsonStr,
                jsonStr,
                N3d.encode(event.getTargetId()));
    }
}
