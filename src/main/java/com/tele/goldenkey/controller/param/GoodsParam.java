package com.tele.goldenkey.controller.param;

import com.tele.goldenkey.domain.Goods;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GoodsParam {

    /**
     * 名称
     */
    @NotEmpty
    private String name;

    /**
     * 商品链接
     */
    @NotEmpty
    private String goodsLink;

    /**
     * 商品描述
     */
    @NotEmpty
    private String description;

    /**
     * 图片地址
     */
    @NotEmpty
    private String pictureLink;

    public Goods convertDao(Integer userId) {
        Goods goods = new Goods();
        goods.setUserId(userId);
        goods.setName(name);
        goods.setGoodsLink(goodsLink);
        goods.setDescription(description);
        goods.setPictureLink(pictureLink);
        return goods;
    }
}
