package com.tele.goldenkey.controller;

import com.google.common.collect.Maps;
import com.tele.goldenkey.event.type.OpenLiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.util.N3d;
import com.tele.goldenkey.util.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@Api(tags = "直播相关")
@RestController
@RequestMapping("/live")
@Slf4j
@RequiredArgsConstructor
public class LiveController extends BaseController {
    private final LiveService liveService;
    private final ApplicationContext applicationContext;

    @ApiOperation(value = "获取直播流推送地址")
    @PostMapping(value = "/get/push-url/{id}")
    public APIResult<HashMap<String, String>> getPushUrl(
            @ApiParam(name = "id", value = "id", required = true, type = "String")
            @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        String pushUrl = liveService.getPushUrl(id);
        ValidateUtils.notEmpty(pushUrl);

        applicationContext.publishEvent(new OpenLiveEvent(getCurrentUserId(), id));
        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("pushUrl", pushUrl);
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
    public APIResult<HashMap<String, Boolean>> close(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                                     @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        HashMap<String, Boolean> hashMap = Maps.newHashMap();
        hashMap.put("close", liveService.close(id));
        return APIResultWrap.ok(hashMap);
    }

    @ApiOperation(value = "获取直播地址")
    @PostMapping(value = "/get/live-url/{id}")
    public APIResult<HashMap<String, String>> getLiveUrl(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                                         @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        HashMap<String, String> hashMap = Maps.newHashMap();
        hashMap.put("liveUrl", liveService.getLiveUrl(id));
        return APIResultWrap.ok(hashMap);
    }
}
