package com.tele.goldenkey.spi.agora;

import com.tele.goldenkey.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        System.out.println(service.startRecording("1511", "15"));
    }

    @Test
    void stopRecording() throws ServiceException {
        System.out.println(service.stopRecording("1511", "15"));
    }


}