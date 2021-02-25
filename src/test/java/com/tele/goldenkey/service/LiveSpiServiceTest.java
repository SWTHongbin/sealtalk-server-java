package com.tele.goldenkey.service;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.spi.live.LiveSpiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveSpiServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void createDataVersion() {

        System.out.println(JSON.toJSONString(applicationContext.getBeansOfType(LiveSpiService.class)));
    }
}