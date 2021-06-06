package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.OptionPriceDto;
import com.tele.goldenkey.dto.PricePackageDto;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
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
     * @param code     1音频 ;2视频
     * @param priceDto
     * @return
     */
    @PostMapping("recharge/{code}")
    public APIResult<Void> recharge(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) throws ServiceException {
        SkuType sku = SkuType.byCodeOf(code);
        return APIResultWrap.ok();
    }

    /**
     * 扣减
     *
     * @param code     1音频 ;2视频
     * @param priceDto
     * @return
     */
    @PostMapping("deduct/{code}")
    public APIResult<PricePackageDto> deduct(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) throws ServiceException {
        SkuType sku = SkuType.byCodeOf(code);
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
