package com.tele.goldenkey.event;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.live.LiveEventCls;
import com.tele.goldenkey.live.LiveEventFactory;
import com.tele.goldenkey.service.UsersService;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.tele.goldenkey.spi.agora.RtmTokenBuilderSample.sendMsgOfBroadcast;
import static com.tele.goldenkey.spi.agora.RtmTokenBuilderSample.sendMsgOfTerminal;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveListener {

    private final UsersService usersService;
    private final ApplicationEventPublisher eventPublisher;


    @Async
    @EventListener
    public void additionalEvent(LiveEvent event) {
        if (event.getMsgType().additionalEvent == null) return;
        for (EventType type : event.getMsgType().additionalEvent) {
            try {
                LiveEventCls liveEventCls = LiveEventFactory.getByKey(type.name());
                ValidateUtils.notNull(liveEventCls);
                LiveEventDto liveEventDto = liveEventCls.getLiveEventDto(event.getLiveId(), event.getFromUserId(), event.getToTerminalId());
                eventPublisher.publishEvent(liveEventCls.execute(liveEventDto));
            } catch (Exception e) {
                log.error("监听事件异常:", e);
            }
        }
    }

    private final static String AGORA_CHANNEL_PREFIX = "agora_";

    @Async
    @EventListener
    public void notice(LiveEvent event) {
        RtmMsgDto rtmMsgDto = new RtmMsgDto();
        rtmMsgDto.setCode(event.getMsgType().code);
        rtmMsgDto.setData(event.getData());
        switch (event.getMsgType().passageway) {
            case terminal:
                if (event.getToTerminalId() != null) {
                    rtmMsgDto.setMessage(usersService.getCurrentUserNickNameWithCache(event.getToTerminalId()) + event.getMsgType().desc);
                }
                log.info("单播消息推送:{}", JSON.toJSONString(rtmMsgDto));
                sendMsgOfTerminal(String.valueOf(event.getFromUserId()), String.valueOf(event.getToTerminalId()), rtmMsgDto);
                return;
            case broadcast:
                if (event.getFromUserId() != null) {
                    rtmMsgDto.setMessage(usersService.getCurrentUserNickNameWithCache(event.getFromUserId()) + event.getMsgType().desc);
                }
                log.info("广播消息推送:{}", JSON.toJSONString(rtmMsgDto));
                sendMsgOfBroadcast(AGORA_CHANNEL_PREFIX + event.getLiveId(), rtmMsgDto);
                return;
        }
    }
}
