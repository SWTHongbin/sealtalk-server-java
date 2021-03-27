package com.tele.goldenkey.dao;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveStatusesMapperTest {
    @Autowired
    private LiveStatusesMapper liveStatusesMapper;

    @Test
    void findByLivedId() {
        System.out.println(JSON.toJSONString(liveStatusesMapper.findByLivedId(12233)));
    }

    @Test
    void closeByLivedId() {
        System.out.println(liveStatusesMapper.closeByLivedId(12233));
    }

    @Test
    void openByLivedId() {
        System.out.println(liveStatusesMapper.openByLivedId(12233));
    }

    @Test
    void noLongerUsed() {
        System.out.println(JSON.toJSONString(liveStatusesMapper.noLongerUsed()));
    }

    @Test
    void updateCount() {
        liveStatusesMapper.updateCount(12233, -11);
    }
}