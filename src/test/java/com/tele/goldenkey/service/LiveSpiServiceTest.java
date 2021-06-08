package com.tele.goldenkey.service;

import com.tele.goldenkey.event.type.LiveEvent;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.live.LiveService;
import com.tele.goldenkey.spi.agora.eums.EventType;
import com.tele.goldenkey.util.N3d;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.core.SdkSystemSetting;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveSpiServiceTest {

    @Autowired
    private LiveService liveSpiService;
    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void getPushUrl() throws ServiceException {
        System.out.println(N3d.encode(1233));

        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(liveSpiService.anchor(12233L));
    }

    @Test
    public void isOpen() {
        System.out.println(liveSpiService.isOpen(12233L));
    }

    @Test
    public void close() {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(liveSpiService.close(12233l));
    }

    @Test
    public void startRecorde() throws ServiceException {
        liveSpiService.startRecorde(5);
    }

    @Test
    public void stopRecorde() throws ServiceException {
        liveSpiService.stopRecorde(5);
    }

    @Test
    public void eventJoin() throws InterruptedException {
        applicationContext.publishEvent(new LiveEvent<Void>(EventType.join, 8L, 1, null));
        Thread.sleep(100000);

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