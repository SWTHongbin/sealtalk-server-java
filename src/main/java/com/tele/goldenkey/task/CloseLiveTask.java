package com.tele.goldenkey.task;

import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.live.LiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CloseLiveTask {

    private final LiveService liveService;

    @Scheduled(cron = "*/9 * * * * ? ")
    public void closeNoPing100s() {
        liveService.openNoPing100s().forEach(
                x -> {
                    try {
                        liveService.close(x);
                    } catch (ServiceException ignore) {
                    }
                }
        );
    }
}
