package com.tele.goldenkey.service;

import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.live.LiveService;
import com.tele.goldenkey.spi.agora.eums.EventType;
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
    private LiveService liveSpiService;
    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void isOpen() {
        System.out.println(liveSpiService.isOpen(12233L));
    }


    @Test
    public void eventJoin() throws InterruptedException {
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.join, 8L, 1, null));
        Thread.sleep(100000);

    }

    @Test
    public void openLive() throws ServiceException {
//        LiveParam liveParam = new LiveParam();
//        liveParam.setRecorde(true);
//        liveParam.setTheme("test");
//        liveParam.setType(1);
//        System.out.println(JSONObject.toJSONString(liveSpiService.openLive(5, liveParam)));
    }

    @Test
    public void liveOption() throws ServiceException {
        System.out.println(liveSpiService.liveOption(104, 105L, true));
    }

    @Test
    public void close() throws ServiceException {
        System.out.println(liveSpiService.close(105L));
    }

    @Test
    public void eventLeave() throws InterruptedException {
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.leave, 8L, 1, null));
        Thread.sleep(100000);
    }

    @Test
    public void eventUpMai() throws InterruptedException {
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.up_mai, 8L, 1, null));
        Thread.sleep(100000);
    }

    @Test
    public void eventDownMai() throws InterruptedException {
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.down_mai, 8L, 1, null));
        Thread.sleep(100000);
    }
}