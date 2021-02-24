package com.tele.goldenkey.controller;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.LiveService;
import com.tele.goldenkey.util.N3d;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "直播相关")
@RestController
@RequestMapping("/live")
@Slf4j
public class LiveController {
    @Resource
    private LiveService liveService;

    @ApiOperation(value = "获取直播流推送地址")
    @PostMapping(value = "/get/push-stream-url/{id}")
    public APIResult<Object> getStreamUrl(
            @ApiParam(name = "id", value = "id", required = true, type = "String")
            @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        return APIResultWrap.ok();
    }

    @ApiOperation(value = "是否开麦")
    @PostMapping(value = "/is-open/{id}")
    public APIResult<Object> isOpen(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                    @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        return APIResultWrap.ok();
    }

    @ApiOperation(value = "关闭直播")
    @PostMapping(value = "/close/{id}")
    public APIResult<Object> close(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                   @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        return APIResultWrap.ok();
    }

    @ApiOperation(value = "获取直播流")
    @PostMapping(value = "/get-stream/{id}")
    public APIResult<Object> getStream(@ApiParam(name = "id", value = "id", required = true, type = "String")
                                       @PathVariable("id") String encodeGroupId) throws ServiceException {
        Integer id = N3d.decode(encodeGroupId);
        return APIResultWrap.ok();
    }
}
