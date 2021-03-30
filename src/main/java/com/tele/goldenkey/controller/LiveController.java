package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.controller.param.MaiEventParam;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.dto.LiveUserDto;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.live.LiveEventCls;
import com.tele.goldenkey.live.LiveEventFactory;
import com.tele.goldenkey.live.LiveService;
import com.tele.goldenkey.live.LiveUserService;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
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
    public APIResult<LiveTokenDto> getPushUrl(@RequestBody @Validated LiveParam liveParam) throws ServiceException {
        Integer livedId = getCurrentUserId();
        liveService.initRoom(liveParam, livedId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(livedId));
        liveTokenDto.setUrl(liveService.getPushUrl(livedId));
        liveTokenDto.setLivedId(livedId);
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + livedId, String.valueOf(livedId), RtcTokenBuilder.Role.Role_Publisher));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + livedId);
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(livedId)));
        applicationContext.publishEvent(new LiveEvent(EventType.open, livedId, livedId));
        return APIResultWrap.ok(liveTokenDto);
    }

    @ApiOperation(value = "是否开播")
    @PostMapping(value = "/is-open/{livedId}")
    public APIResult<HashMap<String, Boolean>> isOpen(@ApiParam(name = "livedId", value = "直播id")
                                                      @PathVariable("livedId") Integer livedId) {
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
    public APIResult<List<LiveUserDto>> user(@ApiParam(name = "livedId", value = "直播id")
                                             @PathVariable("livedId") Integer livedId,
                                             @RequestBody LiveUserParam param) {
        return APIResultWrap.ok(userService.getUsers(param, livedId));
    }

    @ApiOperation(value = "用户离开房间")
    @PostMapping(value = "/leave")
    public APIResult<Void> leave() throws ServiceException {
        Integer id = getCurrentUserId();
        applicationContext.publishEvent(new LiveEvent(EventType.leave, liveService.leave(id), id));
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "直播房间个人信息")
    @PostMapping(value = "/user")
    public APIResult<LiveUserDto> user() throws ServiceException {
        return APIResultWrap.ok(userService.getUser(getCurrentUserId()));
    }

    @ApiOperation(value = "个人开关麦")
    @PostMapping(value = "/mai/{eventName}")
    public APIResult<Void> optionMai(@ApiParam(name = "status", value = "消息类型  up_mai 开 down_mai 关")
                                     @PathVariable("eventName") String eventName) throws ServiceException {
        LiveEvent liveEvent = userService.optionMai(getCurrentUserId(), EventType.valueOf(eventName));
        applicationContext.publishEvent(liveEvent);
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "直播事件")
    @PostMapping(value = "/event/{type}")
    public APIResult<Void> maiEvent(@ApiParam(name = "type", value = "消息类型")
                                    @PathVariable("type") String eventType,
                                    @RequestBody @Validated MaiEventParam param) throws ServiceException {
        LiveEventCls event = LiveEventFactory.getByKey(eventType);
        ValidateUtils.notNull(event);
        LiveEventDto liveEventDto = event.getLiveEventDto(param.getLivedId(), getCurrentUserId(), param.getTerminalId());
        LiveEvent liveEvent = event.execute(liveEventDto);
        applicationContext.publishEvent(liveEvent);
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "获取房间信息")
    @PostMapping(value = "/get/live-url/{livedId}")
    public APIResult<LiveTokenDto> getLiveUrl(@ApiParam(name = "livedId", value = "直播id")
                                              @PathVariable("livedId") Integer livedId) {
        Integer id = getCurrentUserId();
        liveService.join(id, livedId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(livedId));
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + livedId, String.valueOf(id), RtcTokenBuilder.Role.Role_Subscriber));
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(id)));
        liveTokenDto.setUrl(liveService.getLiveUrl(livedId));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + livedId);
        applicationContext.publishEvent(new LiveEvent(EventType.join, livedId, id));
        return APIResultWrap.ok(liveTokenDto);
    }
}
