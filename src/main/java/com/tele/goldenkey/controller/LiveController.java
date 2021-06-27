package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.controller.param.MaiEventParam;
import com.tele.goldenkey.dto.LiveEventDto;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.dto.LiveUserDto;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.live.LiveEventCls;
import com.tele.goldenkey.live.LiveEventFactory;
import com.tele.goldenkey.live.LiveService;
import com.tele.goldenkey.live.LiveUserService;
import com.tele.goldenkey.model.dto.MyLiveDto;
import com.tele.goldenkey.model.dto.PageDto;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.PricePackageService;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import static com.tele.goldenkey.constant.ErrorCode.PARAMETER_ERROR;

/**
 * 直播相关
 */
@RestController
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveController extends BaseController {

    public final static String AGORA_CHANNEL_PREFIX = "agora_";

    private final LiveService liveService;
    private final ApplicationContext applicationContext;
    private final LiveUserService userService;
    private final PricePackageService pricePackageService;

    /**
     * 我的直播列表
     *
     * @return
     */
    @GetMapping("my-list")
    public APIResult<PageDto<MyLiveDto.Resp>> myList(MyLiveDto.Rep rep) throws ServiceException {
        return APIResultWrap.ok(liveService.userLiveList(rep, getCurrentUserId()));
    }

    /**
     * 获取直播流推送地址  或者开播接口
     *
     * @param liveParam
     * @return
     */
    @PostMapping(value = "/get/push-url")
    public APIResult<LiveTokenDto> getPushUrl(@RequestBody @Validated LiveParam liveParam) throws ServiceException {
        Integer userId = getCurrentUserId();
        ValidateUtils.isTrue(pricePackageService.enoughBalance(userId, SkuType.byCodeOf(liveParam.getType())),"余额不足,请先充值");
        Long liveId = liveService.initRoom(userId, liveParam);
        LiveTokenDto liveTokenDto = liveService.liveOption(userId, liveId, liveParam.getRecorde());
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.open, liveTokenDto.getLivedId(), userId, null));
        return APIResultWrap.ok(liveTokenDto);
    }


    /**
     * 是否开播
     *
     * @param livedId 直播id
     * @return
     */
    @PostMapping(value = "/is-open/{livedId}")
    public APIResult<HashMap<String, Boolean>> isOpen(@PathVariable("livedId") Long livedId) {
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("isOpen", liveService.isOpen(livedId));
        return APIResultWrap.ok(hashMap);
    }

    /**
     * 关闭直播
     *
     * @param livedId 直播id
     * @return
     * @throws ServiceException
     */
    @PostMapping(value = "/close/{livedId}")
    public APIResult<Void> close(@PathVariable("livedId") Long livedId) throws ServiceException {
        liveService.close(livedId);
        LiveEventCls event = LiveEventFactory.getByKey(EventType.close.name());
        LiveEventDto liveEventDto = event.getLiveEventDto(livedId, getCurrentUserId(), null);
        applicationContext.publishEvent(event.execute(liveEventDto));
        return APIResultWrap.ok(null, "关闭成功");
    }

    /**
     * 查询直播间用户
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/user")
    public APIResult<List<LiveUserDto>> user(@RequestBody LiveUserParam param) {
        return APIResultWrap.ok(userService.getUsers(param));
    }

    /**
     * 用户离开房间
     *
     * @return
     * @throws ServiceException
     */
    @PostMapping(value = "/leave")
    public APIResult<Void> leave() throws ServiceException {
        Integer id = getCurrentUserId();
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.leave, liveService.leave(id), id, null));
        return APIResultWrap.ok(null, "操作成功");
    }

    /**
     * 直播房间我的个人信息
     *
     * @return
     * @throws ServiceException
     */
    @PostMapping(value = "/my")
    public APIResult<LiveUserDto> user() throws ServiceException {
        return APIResultWrap.ok(userService.getUser(getCurrentUserId()));
    }

    /**
     * 个人操作-(开关麦) (开关语音)
     *
     * @param eventName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws ServiceException
     * @see com.tele.goldenkey.spi.agora.eums.EventType
     */
    @PostMapping(value = "/my/{eventName}")
    public APIResult<Void> optionMai(@PathVariable("eventName") String eventName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ServiceException {
        EventType eventType = EventType.valueOf(eventName);
        ValidateUtils.notNull(eventType.personalFunction);
        Method method = LiveUserService.class.getMethod(eventType.personalFunction.name(), Integer.class, EventType.class);
        applicationContext.publishEvent(method.invoke(userService, getCurrentUserId(), eventType));
        return APIResultWrap.ok(null, "操作成功");
    }


    /**
     * 直播事件
     *
     * @param eventName 消息类型
     * @param param
     * @return
     * @throws ServiceException
     * @see com.tele.goldenkey.spi.agora.eums.EventType
     */
    @PostMapping(value = "/event/{eventName}")
    public APIResult<Void> maiEvent(@PathVariable("eventName") String eventName,
                                    @RequestBody @Validated MaiEventParam param) throws ServiceException {
        LiveEventCls event = LiveEventFactory.getByKey(eventName);
        ValidateUtils.notNull(event);
        LiveEventDto liveEventDto = event.getLiveEventDto(param.getLivedId(), getCurrentUserId(), param.getTerminalId());
        applicationContext.publishEvent(event.execute(liveEventDto));
        return APIResultWrap.ok(null, "操作成功");
    }

    /**
     * 获取房间信息
     *
     * @param livedId 直播id
     * @return
     * @throws ServiceException
     */
    @PostMapping(value = "/get/live-url/{livedId}")
    public APIResult<LiveTokenDto> getLiveUrl(@PathVariable("livedId") Long livedId) throws ServiceException {
        Integer userId = getCurrentUserId();
        liveService.join(userId, livedId);
        String channelName = AGORA_CHANNEL_PREFIX + livedId;
        LiveTokenDto liveTokenDto = new LiveTokenDto()
                .setRoomDto(liveService.room(livedId, userId))
                .setRtcToken(RtcTokenBuilderSample.buildRtcToken(channelName, String.valueOf(userId), RtcTokenBuilder.Role.Role_Subscriber))
                .setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)))
                .setUserId(userId)
                .setChannelId(channelName);
        applicationContext.publishEvent(LiveEventFactory.getByKey(EventType.join.name()).execute(new LiveEventDto(livedId, userId, liveTokenDto.getRoomDto().getAnchorId())));
        return APIResultWrap.ok(liveTokenDto);
    }

    /**
     * 获取录制url
     *
     * @param livedId 房间id
     * @return
     */
    @GetMapping(value = "/record-url/{livedId}")
    public APIResult<String> playback(@PathVariable("livedId") Long livedId) {
        return APIResultWrap.ok(liveService.recordUrl(livedId));
    }
}
