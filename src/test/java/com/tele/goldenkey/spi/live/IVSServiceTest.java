package com.tele.goldenkey.spi.live;

import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.services.ivs.model.Channel;

@SpringBootTest
class IVSServiceTest {
    @Autowired
    private IVSClient ivsClient;

    @Before
    public void before() {
//        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
//        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
    }

    @Test
    void createChannel() {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        Channel tanyue = ivsClient.createChannel("tanyue");
        System.out.println(JSON.toJSONString(tanyue));
    }

    @Test
    void stopStream() {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(JSON.toJSONString(ivsClient.stopStream("arn:aws:ivs:us-east-1:339989653424:channel/ckjYMFGp4jB2")));
    }

    @Test
    void hello() {
        RtmMsgDto rtmMsgDto = new RtmMsgDto();
        rtmMsgDto.setType(1);
        rtmMsgDto.setMessage("hello");
        RtmTokenBuilderSample.sendMsgOfChannel("admin_123", rtmMsgDto);
    }


    @Test
    void removeChannel() {
        System.setProperty(SdkSystemSetting.AWS_ACCESS_KEY_ID.property(), "AKIAU6KHWM6YI5ZPBFVF");
        System.setProperty(SdkSystemSetting.AWS_SECRET_ACCESS_KEY.property(), "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");
        System.out.println(JSON.toJSONString(ivsClient.removeChannel("arn:aws:ivs:us-east-1:339989653424:channel/ckjYMFGp4jB2")));
    }
}