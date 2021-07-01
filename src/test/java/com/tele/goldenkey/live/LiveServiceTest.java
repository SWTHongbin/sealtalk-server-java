package com.tele.goldenkey.live;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.model.dto.MyLiveDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class LiveServiceTest {
    @Autowired
    private LiveService liveService;

    @Test
    void getMapper() {
    }

    @Test
    void userLiveList() throws ServiceException {
        System.out.println(liveService.userLiveList(new MyLiveDto.Rep(), 1116));
    }

    @Test
    void liveOption() {
    }
}