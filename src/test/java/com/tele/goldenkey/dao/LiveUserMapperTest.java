package com.tele.goldenkey.dao;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class LiveUserMapperTest {
    @Autowired
    private LiveUserMapper liveUserMapper;

    @Test
    void deleteByLivedId() {
        liveUserMapper.selectByUserId(1);
    }

    @Test
    void deleteByUserId() {
        liveUserMapper.deleteByUserId(1);
    }

    @Test
    void selectByUserId() {
        liveUserMapper.selectByUserId(1);
    }

    @Test
    void updateMai() {
        liveUserMapper.updateMai(1, 1, 1);
    }
}