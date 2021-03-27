package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.event.type.LeaveEvent;
import com.tele.goldenkey.event.type.OpenLiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.N3d;
import com.tele.goldenkey.util.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@Api(tags = "直播相关")
@RestController
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveController extends BaseController {
    private final LiveService liveService;
    private final ApplicationContext applicationContext;
    private final static String AGORA_CHANNEL_PREFIX = "agora_";

    @ApiOperation(value = "获取直播流推送地址")
    @PostMapping(value = "/get/push-url/{id}")
    public APIResult<HashMap<String, Object>> getPushUrl(
            @ApiParam(name = "id", value = "id", required = true, type = "String")
            @PathVariable("id") String encodeGroupId,
            @RequestBody LiveParam liveParam) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        String pushUrl = liveService.getPushUrl(id);
        ValidateUtils.notEmpty(pushUrl);
        liveService.initRoom(liveParam, id);

        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.putAll(liveService.room(id));
        hashMap.put("pushUrl", pushUrl);
        hashMap.put("token", RtcTokenBuilderSample.buildToken(AGORA_CHANNEL_PREFIX + id, String.valueOf(getCurrentUserId()), RtcTokenBuilder.Role.Role_Publisher));
        hashMap.put("channelId", AGORA_CHANNEL_PREFIX + id);
        applicationContext.publishEvent(new OpenLiveEvent(getCurrentUserId(), id));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "是否开播")
    @PostMapping(value = "/is-open/{id}")
    public APIResult<HashMap<String, Boolean>> isOpen(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                                      @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("isOpen", liveService.isOpen(id));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "关闭直播")
    @PostMapping(value = "/close/{id}")
    public APIResult<Void> close(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                 @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        liveService.close(id);
        return APIResultWrap.ok();
    }

    @ApiOperation(value = "用户离开房间")
    @PostMapping(value = "/leave/{id}")
    public APIResult<HashMap<String, Boolean>> leave(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                                     @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        liveService.leave(id, -1);
        applicationContext.publishEvent(new LeaveEvent(getCurrentUserId(), id));
        return APIResultWrap.ok();
    }

    @ApiOperation(value = "获取直播地址")
    @PostMapping(value = "/get/live-url/{id}")
    public APIResult<HashMap<String, Object>> getLiveUrl(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                                         @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        liveService.leave(id, 1);
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.putAll(liveService.room(id));
        hashMap.put("token", RtcTokenBuilderSample.buildToken(AGORA_CHANNEL_PREFIX + id, String.valueOf(getCurrentUserId()), RtcTokenBuilder.Role.Role_Subscriber));
        hashMap.put("channelId", AGORA_CHANNEL_PREFIX + id);
        hashMap.put("liveUrl", liveService.getLiveUrl(id));
        return APIResultWrap.ok(hashMap);
    }
}
