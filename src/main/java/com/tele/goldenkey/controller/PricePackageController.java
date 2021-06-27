package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.OptionPriceDto;
import com.tele.goldenkey.domain.UserPricePackage;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.PricePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 套餐价格组件
 */
@RestController
@RequestMapping("/price-package")
@RequiredArgsConstructor
public class PricePackageController extends BaseController {

    private final PricePackageService pricePackageService;

//    /**
//     * 充值
//     *
//     * @param code     1音频 ;2视频
//     * @param priceDto
//     * @return 剩余  单位秒
//     */
//    @PostMapping("recharge/{code}")
//    public APIResult<BigDecimal> recharge(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) throws ServiceException {
//        return APIResultWrap.ok(pricePackageService.recharge(SkuType.byCodeOf(code), getCurrentUserId(), priceDto.getSecond()));
//    }

    /**
     * 扣减
     *
     * @param code     1音频 ;2视频
     * @param priceDto
     * @return 剩余  单位秒
     */
    @PostMapping("deduct/{code}")
    public APIResult<BigDecimal> deduct(@PathVariable Integer code, @RequestBody @Validated OptionPriceDto priceDto) throws ServiceException {
        return APIResultWrap.ok(pricePackageService.deduct(SkuType.byCodeOf(code), getCurrentUserId(), priceDto.getSecond()));
    }

    /**
     * 余额
     *
     * @return
     */
    @GetMapping("balance")
    public APIResult<UserPricePackage> balance() {
        return APIResultWrap.ok(pricePackageService.getPricePackage(getCurrentUserId()));
    }
}
