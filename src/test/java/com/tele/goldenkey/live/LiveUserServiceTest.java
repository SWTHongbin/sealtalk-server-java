package com.tele.goldenkey.live;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.controller.param.LiveUserParam;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class LiveUserServiceTest {
    @Autowired
    private LiveUserService userService;

    @Test
    void getUsers() {
        LiveUserParam param = new LiveUserParam();
        param.setUserId(7);
        System.out.println(JSON.toJSONString(userService.getUsers(param)));
    }

    @Test
    void getUser() {
        System.out.println(JSON.toJSONString(userService.getUser(5)));
    }
}