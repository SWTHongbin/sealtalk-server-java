package com.tele.goldenkey.controller.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DelGoodsParam {
    @NotNull
    private List<Long> ids;
}
