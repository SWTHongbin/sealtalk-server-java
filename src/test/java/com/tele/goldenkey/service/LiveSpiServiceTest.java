package com.tele.goldenkey.service;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.util.N3d;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.amazon.awssdk.core.SdkSystemSetting;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveSpiServiceTest {

    @Autowired
    private LiveService liveSpiService;

    @Test
    public void getPushUrl() throws ServiceException {
        System.out.println(N3d.encode(1233));

        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(liveSpiService.getPushUrl(12233));
    }

    @Test
    public void isOpen() {
        System.out.println(liveSpiService.isOpen(12233));
    }

    @Test
    public void close() {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(liveSpiService.close(12233));
    }

    @Test
    public void getLiveUrl() {
        System.out.println(liveSpiService.getLiveUrl(12233));
    }
}