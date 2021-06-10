package com.tele.goldenkey.spi.agora;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.tele.goldenkey.spi.agora.AgoraRecordingService.CNAME_PREFIX;

@RunWith(SpringRunner.class)
@SpringBootTest
class AgoraRecordingServiceTest {
    @Autowired
    private AgoraRecordingService service;

    @Test
    void getResourceId() {
        // service.getResourceId("151", "15");
    }

    @Test
    void startRecording() throws ServiceException {
        service.startRecording(CNAME_PREFIX + "82", String.valueOf(82) + RandomUtil.randomBetween(10000, 99999));
    }

    @Test
    void stopRecording() throws ServiceException {
        System.out.println(service.stopRecording("1511"));
    }


}