package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.dto.LiveUserDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.service.LiveUserService;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Api(tags = "直播相关")
@RestController
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveController extends BaseController {
    private final LiveService liveService;
    private final ApplicationContext applicationContext;
    private final LiveUserService userService;
    public final static String AGORA_CHANNEL_PREFIX = "agora_";

    @ApiOperation(value = "获取直播流推送地址")
    @PostMapping(value = "/get/push-url")
    public APIResult<LiveTokenDto> getPushUrl(@RequestBody LiveParam liveParam) throws ServiceException {
        Integer userId = getCurrentUserId();
        liveService.initRoom(liveParam, userId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(userId));
        liveTokenDto.setUrl(liveService.getPushUrl(userId));
        liveTokenDto.setLivedId(userId);
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + userId, String.valueOf(userId), RtcTokenBuilder.Role.Role_Publisher));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + userId);
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)));
        applicationContext.publishEvent(new LiveEvent(EventType.open, userId, userId));
        return APIResultWrap.ok(liveTokenDto);
    }

    @ApiOperation(value = "是否开播")
    @PostMapping(value = "/is-open/{livedId}")
    public APIResult<HashMap<String, Boolean>> isOpen(@PathVariable("livedId") Integer livedId) {
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("isOpen", liveService.isOpen(livedId));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "关闭直播")
    @PostMapping(value = "/close")
    public APIResult<Void> close() {
        Integer id = getCurrentUserId();
        liveService.close(id);
        applicationContext.publishEvent(new LiveEvent(EventType.close, id, id));
        return APIResultWrap.ok(null, "关闭成功");
    }

    @ApiOperation(value = "查询直播间用户")
    @PostMapping(value = "/user/{livedId}")
    public APIResult<List<LiveUserDto>> user(@RequestBody LiveUserParam param, @PathVariable("livedId") Integer livedId) {
        Integer id = getCurrentUserId();
        return APIResultWrap.ok( userService.getUsers(param), "关闭成功");
    }

    @ApiOperation(value = "用户离开房间")
    @PostMapping(value = "/leave")
    public APIResult<Void> leave() {
        Integer id = getCurrentUserId();
        liveService.leave(id);
        applicationContext.publishEvent(new LiveEvent(EventType.leave, id, id));
        return APIResultWrap.ok(null, "操作成功");
    }


    @ApiOperation(value = "直播事件")
    @PostMapping(value = "/event/{type}")
    public APIResult<Void> maiEvent(@PathVariable EventType eventType) {
        //todo
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "获取房间信息")
    @PostMapping(value = "/get/live-url/{livedId}")
    public APIResult<LiveTokenDto> getLiveUrl(@PathVariable("livedId") Integer livedId) {
        Integer id = getCurrentUserId();
        liveService.join(id, livedId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(id));
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + id, String.valueOf(id), RtcTokenBuilder.Role.Role_Subscriber));
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(id)));
        liveTokenDto.setUrl(liveService.getLiveUrl(id));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + id);
        applicationContext.publishEvent(new LiveEvent(EventType.join, id, id));
        return APIResultWrap.ok(liveTokenDto);
    }
}
