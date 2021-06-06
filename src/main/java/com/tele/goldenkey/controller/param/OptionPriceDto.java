package com.tele.goldenkey.controller.param;

import com.sun.istack.internal.NotNull;
import lombok.Data;


@Data
public class OptionPriceDto {
    /**
     * 秒数
     */
    @NotNull
    private Long second;
}
