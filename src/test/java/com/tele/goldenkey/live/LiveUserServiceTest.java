package com.tele.goldenkey.live;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.util.N3d;
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
        param.setLivedId(8L);
        param.setMaiStatus(1);
        System.out.println(JSON.toJSONString(userService.getUsers(param)));
    }

    @Test
    void getUser() {
        System.out.println(JSON.toJSONString(userService.getUser(5)));
    }
    void myist() {
        System.out.println(JSON.toJSONString(userService.getUser(5)));
    }
    @Test
    void decode() throws ServiceException {
        System.out.println(N3d.decode("iiq6ugIY2"));
    }
}