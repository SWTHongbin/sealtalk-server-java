package com.tele.goldenkey.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class PrepareOrderParam implements Serializable {

    /**
     * 直播方式
     *
     * @see com.tele.goldenkey.enums.SkuType
     */
    @NotNull
    private Integer type;

    /**
     * 套餐id
     */
    @NotNull
    private Integer packageId;

    private String orderNo;

    private Integer userId;
}
