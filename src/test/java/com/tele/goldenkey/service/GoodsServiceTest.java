package com.tele.goldenkey.service;

import com.alibaba.fastjson.JSONObject;
import com.tele.goldenkey.domain.Goods;
import com.tele.goldenkey.model.dto.SearchPageDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class GoodsServiceTest {
    @Autowired
    private GoodsService goodsService;

    @Test
    void list() {
    }

    @Test
    void add() {
        Goods goods = new Goods();
        goods.setUserId(1);
        goods.setGoodsLink("qwe");
        goods.setPictureLink("123");
        System.out.println(goodsService.saveSelective(goods) > 0);
    }

    @Test
    void del() {
        Goods goods = new Goods();
        goods.setUserId(1);
        goods.setId(1L);
        System.out.println(goodsService.delete(goods) > 0);
    }

    @Test
    void page() {
        System.out.println(JSONObject.toJSONString(goodsService.list(1, new SearchPageDto())));
    }
}