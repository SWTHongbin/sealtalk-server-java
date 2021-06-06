package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.OptionPriceDto;
import com.tele.goldenkey.dto.PricePackageDto;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 套餐价格组件
 */
@RestController
@RequestMapping("/price-package")
@RequiredArgsConstructor
public class PricePackageController extends BaseController {

    /**
     * 充值
     *
     * @return
     */
    @PostMapping("recharge/{code}")
    public APIResult<Void> recharge(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) {
        return APIResultWrap.ok();
    }

    /**
     * 扣减
     *
     * @return
     */
    @PostMapping("deduct/{code}")
    public APIResult<PricePackageDto> deduct(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) {
        return APIResultWrap.ok(PricePackageDto.getInstance());
    }

    /**
     * 余额
     *
     * @return
     */
    @GetMapping("balance")
    public APIResult<PricePackageDto> balance() {
        return APIResultWrap.ok(PricePackageDto.getInstance());
    }
}
