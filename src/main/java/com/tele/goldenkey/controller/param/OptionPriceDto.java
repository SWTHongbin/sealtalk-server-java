package com.tele.goldenkey.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class OptionPriceDto {
    /**
     * 数值  单位秒
     */
    @NotNull
    private BigDecimal second;
}
