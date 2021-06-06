package com.tele.goldenkey.service;

import com.tele.goldenkey.enums.SkuType;
import com.tele.goldenkey.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
class PricePackageServiceTest {
    @Autowired
    private PricePackageService pricePackageService;

    @Test
    void recharge() throws ServiceException {
        System.out.println(pricePackageService.recharge(SkuType.audio, 123, BigDecimal.valueOf(1000 * 50 * 100)));
    }

    @Test
    void deduct() throws ServiceException {
        System.out.println(pricePackageService.deduct(SkuType.audio, 123, BigDecimal.valueOf(1000 * 50 * 100)));
    }

    @Test
    void getPricePackage() {
        System.out.println(pricePackageService.getPricePackage(123));
    }
}