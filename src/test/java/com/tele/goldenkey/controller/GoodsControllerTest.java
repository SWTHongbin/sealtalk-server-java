package com.tele.goldenkey.controller;

import com.tele.goldenkey.domain.Goods;
import com.tele.goldenkey.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class GoodsControllerTest {
    @Autowired
    private GoodsService goodsService;

    @Test
    void list() {
    }

    @Test
    void delById() {
    }

    @Test
    void delByIds() {
    }

    @Test
    void delAllByUserId() {
    }

    @Test
    void add() {
        Goods goods = new Goods();
        goods.setUserId(1);
        goods.setGoodsLink("www.baidu.com");
        goods.setName("test");
        goodsService.saveSelective(goods);
    }
}