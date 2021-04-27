package com.tele.goldenkey.manager;

import com.alibaba.fastjson.JSON;
import com.qiniu.util.Json;
import com.tele.goldenkey.domain.Users;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.util.MiscUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserManagerTest {
    @Autowired
    private UserManager userManager;

    @Test
    void sendCode() {
    }

    @Test
    void isExistUser() {
    }

    @Test
    void verifyCode() {
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void setNickName() {
    }

    @Test
    void setPortraitUri() {
    }

    @Test
    void getUser() {
        Users user = userManager.getUser("1", "9168424188");
        if (user != null && Users.PHONE_VERIFY_NO_NEED.equals(user.getPhoneVerify())) {
            System.out.println("has one");
        }

    }
}