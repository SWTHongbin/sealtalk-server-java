package com.tele.goldenkey.event;

import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.service.UsersService;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.tele.goldenkey.controller.LiveController.AGORA_CHANNEL_PREFIX;
import static com.tele.goldenkey.spi.agora.RtmTokenBuilderSample.sendMsgOfBroadcast;
import static com.tele.goldenkey.spi.agora.RtmTokenBuilderSample.sendMsgOfTerminal;

@Component
@RequiredArgsConstructor
public class LiveListener {
    private final UsersService usersService;

    @Async
    @EventListener
    public void leave(LiveEvent event) {
        RtmMsgDto rtmMsgDto = new RtmMsgDto();
        rtmMsgDto.setType(event.getMsgType().code);
        rtmMsgDto.setMessage(usersService.getCurrentUserNickNameWithCache(event.getTerminalId()) + event.getMsgType().desc);
        switch (event.getMsgType().passageway) {
            case terminal:
                sendMsgOfTerminal(String.valueOf(event.getLivedId()), String.valueOf(event.getTerminalId()), rtmMsgDto);
                break;
            case broadcast:
                sendMsgOfBroadcast(AGORA_CHANNEL_PREFIX + event.getLivedId(), rtmMsgDto);
                break;
        }

    }
}
