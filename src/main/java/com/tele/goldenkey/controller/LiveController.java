package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.eums.RtmMsgType;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@Api(tags = "直播相关")
@RestController
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveController extends BaseController {
    private final LiveService liveService;
    private final ApplicationContext applicationContext;
    public final static String AGORA_CHANNEL_PREFIX = "agora_";

    @ApiOperation(value = "获取直播流推送地址")
    @PostMapping(value = "/get/push-url")
    public APIResult<LiveTokenDto> getPushUrl(@RequestBody LiveParam liveParam) throws ServiceException {
        Integer userId = getCurrentUserId();
        String pushUrl = liveService.getPushUrl(userId);
        ValidateUtils.notEmpty(pushUrl);
        liveService.initRoom(liveParam, userId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(userId));
        liveTokenDto.setUrl(pushUrl);
        liveTokenDto.setAgoroId(userId);
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildToken(AGORA_CHANNEL_PREFIX + userId, String.valueOf(userId), RtcTokenBuilder.Role.Role_Publisher));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + userId);
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)));
        applicationContext.publishEvent(new LiveEvent(RtmMsgType.open, userId, userId));
        return APIResultWrap.ok(liveTokenDto);
    }

    @ApiOperation(value = "是否开播")
    @PostMapping(value = "/is-open")
    public APIResult<HashMap<String, Boolean>> isOpen() {
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("isOpen", liveService.isOpen(getCurrentUserId()));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "关闭直播")
    @PostMapping(value = "/close")
    public APIResult<Void> close() {
        Integer id = getCurrentUserId();
        liveService.close(id);
        applicationContext.publishEvent(new LiveEvent(RtmMsgType.close, id, id));
        return APIResultWrap.ok(null, "关闭成功");
    }

    @ApiOperation(value = "用户离开房间")
    @PostMapping(value = "/leave")
    public APIResult<Void> leave() {
        Integer id = getCurrentUserId();
        liveService.leave(id, -1);
        applicationContext.publishEvent(new LiveEvent(RtmMsgType.leave, id, id));
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "获取房间信息")
    @PostMapping(value = "/get/live-url")
    public APIResult<LiveTokenDto> getLiveUrl() {
        Integer id = getCurrentUserId();
        liveService.leave(id, 1);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(id));
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildToken(AGORA_CHANNEL_PREFIX + id, String.valueOf(id), RtcTokenBuilder.Role.Role_Subscriber));
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(id)));
        liveTokenDto.setUrl(liveService.getLiveUrl(id));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + id);
        applicationContext.publishEvent(new LiveEvent(RtmMsgType.join, id, id));
        return APIResultWrap.ok(liveTokenDto);
    }
}
