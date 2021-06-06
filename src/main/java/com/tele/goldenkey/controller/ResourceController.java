package com.tele.goldenkey.controller;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.dto.PricePackageDto;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源组件
 */
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    /**
     * 定价套餐
     *
     * @return
     */
    @GetMapping("price-package")
    public APIResult<PricePackageDto> pricePackage() {
        return APIResultWrap.ok(PricePackageDto.getInstance());
    }

}
