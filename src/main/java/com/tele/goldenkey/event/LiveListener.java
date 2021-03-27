package com.tele.goldenkey.event;

import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.manager.MiscManager;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.service.UsersService;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.tele.goldenkey.controller.LiveController.AGORA_CHANNEL_PREFIX;

@Component
@RequiredArgsConstructor
public class LiveListener {
    private final MiscManager miscManager;
    private final UsersService usersService;
    private final LiveService liveService;

//    @Async
//    @EventListener
//    public void openLive(OpenLiveEvent event) throws ServiceException {
//        String liveUrl = liveService.getLiveUrl(event.getTargetId());
//        if (StringUtils.isBlank(liveUrl)) return;
//
//        HashMap<String, String> hashMap = Maps.newHashMap();
//        hashMap.put("username", usersService.getCurrentUserNickNameWithCache(event.getCurrentUserId()));
//        hashMap.put("liveUrl", liveUrl);
//        String jsonStr = JSON.toJSONString(hashMap);
//        miscManager.sendMessage(event.getCurrentUserId(),
//                Constants.CONVERSATION_TYPE_GROUP,
//                event.getTargetId(),
//                "1",
//                jsonStr,
//                jsonStr,
//                N3d.encode(event.getTargetId()));
//    }

    //    @Async
//    @EventListener
//    public void leave(LeaveEvent event) throws ServiceException {
//        HashMap<String, String> hashMap = Maps.newHashMap();
//        hashMap.put("username", usersService.getCurrentUserNickNameWithCache(event.getCurrentUserId()));
//        hashMap.put("message", "离开房间");
//        hashMap.put("code", "3");
//        String jsonStr = JSON.toJSONString(hashMap);
//        miscManager.sendMessage(event.getCurrentUserId(),
//                Constants.CONVERSATION_TYPE_GROUP,
//                event.getTargetId(),
//                "1",
//                jsonStr,
//                jsonStr,
//                N3d.encode(event.getTargetId()));
//    }
    @Async
    @EventListener
    public void leave(LiveEvent event) {
        RtmMsgDto rtmMsgDto = new RtmMsgDto();
        rtmMsgDto.setType(event.getMsgType().code);
        rtmMsgDto.setMessage(usersService.getCurrentUserNickNameWithCache(event.getUserId()) + event.getMsgType().dsc);
        RtmTokenBuilderSample.sendMsg(AGORA_CHANNEL_PREFIX + event.getLivedId(), rtmMsgDto);
    }
}
