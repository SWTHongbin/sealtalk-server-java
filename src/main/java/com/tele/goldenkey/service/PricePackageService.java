package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.UserPricePackageMapper;
import com.tele.goldenkey.domain.UserPricePackage;
import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class PricePackageService extends AbstractBaseService<UserPricePackage, Long> {

    private final UserPricePackageMapper pricePackageMapper;

    public BigDecimal recharge(SkuType sku, Integer userId, BigDecimal second) throws ServiceException {
        UserPricePackage pricePackage = getPricePackage(userId);
        if (pricePackage == null) {
            createPricePackage(sku, userId, second);
            return second;
        }
        ValidateUtils.isTrue(optionBalance(sku, userId, second) > 0);
        return getBalance(sku, pricePackage);
    }

    public BigDecimal deduct(SkuType sku, Integer userId, BigDecimal second) throws ServiceException {
        ValidateUtils.isTrue(optionBalance(sku, userId, second.negate()) > 0);
        return getBalance(sku, getPricePackage(userId));
    }

    public boolean enoughBalance(Integer userId, SkuType skuType) {
        return getBalance(skuType, getPricePackage(userId)).compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getBalance(Integer userId, SkuType skuType) {
        return getBalance(skuType, getPricePackage(userId));
    }

    public UserPricePackage getPricePackage(Integer userId) {
        return pricePackageMapper.findByUserId(userId);
    }

    @Override
    protected Mapper<UserPricePackage> getMapper() {
        return pricePackageMapper;
    }

    private static BigDecimal getBalance(SkuType sku, UserPricePackage userPricePackage) {
        if (userPricePackage == null) return BigDecimal.ZERO;
        switch (sku) {
            case audio:
                return userPricePackage.getAudioBalance();
            case video:
                return userPricePackage.getVideoBalance();
        }
        return BigDecimal.ZERO;
    }

    private void createPricePackage(SkuType sku, Integer userId, BigDecimal second) {
        UserPricePackage pricePackage = new UserPricePackage();
        pricePackage.setUserId(userId);
        if (sku == SkuType.audio) {
            pricePackage.setAudioBalance(second);
        } else {
            pricePackage.setVideoBalance(second);
        }
        this.saveSelective(pricePackage);
    }

    private Integer optionBalance(SkuType sku, Integer userId, BigDecimal second) {
        switch (sku) {
            case video:
                return pricePackageMapper.optionVideoBalance(userId, second);
            case audio:
                return pricePackageMapper.optionAudioBalance(userId, second);
        }
        return 0;
    }
}
