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
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

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
    public APIResult<LiveTokenDto> getPushUrl(@RequestBody @Validated LiveParam liveParam) {
        Integer userId = getCurrentUserId();
        Long liveId = liveService.initRoom(userId, liveParam);
        LiveTokenDto liveTokenDto = liveService.anchor(liveId);
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + userId, String.valueOf(userId), RtcTokenBuilder.Role.Role_Publisher));
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + userId);
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)));
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.open, liveId, userId, null));
        return APIResultWrap.ok(liveTokenDto);
    }

    @ApiOperation(value = "是否开播")
    @PostMapping(value = "/is-open/{livedId}")
    public APIResult<HashMap<String, Boolean>> isOpen(@ApiParam(name = "livedId", value = "直播id")
                                                      @PathVariable("livedId") Long livedId) {
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("isOpen", liveService.isOpen(livedId));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "关闭直播")
    @PostMapping(value = "/close/{livedId}")
    public APIResult<Void> close(@PathVariable("livedId") Long livedId) throws ServiceException {
        liveService.close(livedId);
        LiveEventCls event = LiveEventFactory.getByKey(EventType.close.name());
        LiveEventDto liveEventDto = event.getLiveEventDto(livedId, getCurrentUserId(), null);
        applicationContext.publishEvent(event.execute(liveEventDto));
        return APIResultWrap.ok(null, "关闭成功");
    }

    @ApiOperation(value = "查询直播间用户")
    @PostMapping(value = "/user")
    public APIResult<List<LiveUserDto>> user(@RequestBody LiveUserParam param) {
        return APIResultWrap.ok(userService.getUsers(param));
    }

    @ApiOperation(value = "用户离开房间")
    @PostMapping(value = "/leave")
    public APIResult<Void> leave() throws ServiceException {
        Integer id = getCurrentUserId();
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.leave, liveService.leave(id), id, null));
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "直播房间我的个人信息")
    @PostMapping(value = "/my")
    public APIResult<LiveUserDto> user() throws ServiceException {
        return APIResultWrap.ok(userService.getUser(getCurrentUserId()));
    }

    @ApiOperation(value = "个人操作-(开关麦) (开关语音)")
    @PostMapping(value = "/my/{eventName}")
    public APIResult<Void> optionMai(@ApiParam(name = "eventName", value = "消息类型  up_mai 开 down_mai 关")
                                     @PathVariable("eventName") String eventName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ServiceException {
        EventType eventType = EventType.valueOf(eventName);
        ValidateUtils.notNull(eventType.personalFunction);
        Method method = LiveUserService.class.getMethod(eventType.personalFunction.name(), Integer.class, EventType.class);
        applicationContext.publishEvent(method.invoke(userService, getCurrentUserId(), eventType));
        return APIResultWrap.ok(null, "操作成功");
    }


    @ApiOperation(value = "直播事件")
    @PostMapping(value = "/event/{eventName}")
    public APIResult<Void> maiEvent(@ApiParam(name = "eventName", value = "消息类型")
                                    @PathVariable("eventName") String eventName,
                                    @RequestBody @Validated MaiEventParam param) throws ServiceException {
        LiveEventCls event = LiveEventFactory.getByKey(eventName);
        ValidateUtils.notNull(event);
        LiveEventDto liveEventDto = event.getLiveEventDto(param.getLivedId(), getCurrentUserId(), param.getTerminalId());
        applicationContext.publishEvent(event.execute(liveEventDto));
        return APIResultWrap.ok(null, "操作成功");
    }

    @ApiOperation(value = "获取房间信息")
    @PostMapping(value = "/get/live-url/{livedId}")
    public APIResult<LiveTokenDto> getLiveUrl(@ApiParam(name = "livedId", value = "直播id")
                                              @PathVariable("livedId") Long livedId) throws ServiceException {
        Integer userId = getCurrentUserId();
        liveService.join(userId, livedId);
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(liveService.room(livedId));
        liveTokenDto.setRtcToken(RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + userId, String.valueOf(userId), RtcTokenBuilder.Role.Role_Subscriber));
        liveTokenDto.setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)));
        liveTokenDto.setUserId(userId);
        liveTokenDto.setChannelId(AGORA_CHANNEL_PREFIX + livedId);
        applicationContext.publishEvent(LiveEventFactory.getByKey(EventType.join.name()).execute(new LiveEventDto(livedId, userId, liveTokenDto.getRoomDto().getAnchorId())));
        return APIResultWrap.ok(liveTokenDto);
    }
}
