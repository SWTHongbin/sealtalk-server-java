package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.UserPricePackageMapper;
import com.tele.goldenkey.domain.UserPricePackage;
import com.tele.goldenkey.enums.SkuType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class PricePackageService extends AbstractBaseService<UserPricePackage, Long> {

    private final UserPricePackageMapper pricePackageMapper;

    public BigDecimal recharge(SkuType sku, Integer userId, BigDecimal second) {
        UserPricePackage pricePackage = getPricePackage(userId);
        if (pricePackage == null) {
            createPricePackage(sku, userId, second);
            return second;
        }
        optionBalance(sku, userId, second);
        return getBalance(sku, pricePackage);
    }


    public BigDecimal deduct(SkuType sku, Integer userId, BigDecimal second) {
        optionBalance(sku, userId, second.negate());
        return getBalance(sku, getPricePackage(userId));
    }


    public UserPricePackage getPricePackage(Integer userId) {
        return pricePackageMapper.findByUserId(userId);
    }

    @Override
    protected Mapper<UserPricePackage> getMapper() {
        return pricePackageMapper;
    }

    public static BigDecimal getBalance(SkuType sku, UserPricePackage userPricePackage) {
        switch (sku) {
            case audio:
                return userPricePackage.getAudioBalance();
            case video:
                return userPricePackage.getVideoBalance();
        }
        return BigDecimal.ZERO;
    }

    private void createPricePackage(SkuType sku, Integer userId, BigDecimal second) {
        UserPricePackage pricePackage;
        pricePackage = new UserPricePackage();
        pricePackage.setUserId(userId);
        if (sku == SkuType.audio) {
            pricePackage.setAudioBalance(second);
        } else {
            pricePackage.setVideoBalance(second);
        }
        this.saveSelective(pricePackage);
    }

    private void optionBalance(SkuType sku, Integer userId, BigDecimal second) {
        switch (sku) {
            case video:
                pricePackageMapper.optionVideoBalance(userId, second);
                return;
            case audio:
                pricePackageMapper.optionAudioBalance(userId, second);
        }
    }
}
