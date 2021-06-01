//package com.tele.goldenkey.task;
//
//import com.tele.goldenkey.dao.LiveStatusesMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class DelChannelStream {
//    private final LiveStatusesMapper liveStatusesMapper;
//
//    @Scheduled(cron = "0 0 1 1 * ?")
//    public void removeChannel() {
//        log.info("begin  clear channel");
//        liveStatusesMapper.noLongerUsed()
//                .forEach(
//                        x -> {
//                            try {
//                                ivsClient.removeChannel(x.getCode());
//                            } catch (Exception e) {
//                                log.error("clear channel  id:" + x.getLiveId(), e);
//                            }
//                            liveStatusesMapper.deleteByPrimaryKey(x.getLiveId());
//                        });
//
//    }
//}
