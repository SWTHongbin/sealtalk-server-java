package com.tele.goldenkey.controller.param;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class OptionPriceDto {
    /**
     * 数值  单位秒
     */
    @NotNull
    private BigDecimal second;
}
