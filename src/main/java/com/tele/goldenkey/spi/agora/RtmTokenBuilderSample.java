package com.tele.goldenkey.spi.agora;


import com.tele.goldenkey.spi.agora.rtm.RtmTokenBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RtmTokenBuilderSample {
    private final static String appId = "a75d7dfc56454049aa425f39b085db94";
    private final static String appCertificate = "a0a62266ac204e9eae02add460fabcbd";

    public static String buildRtmToken(String userId) {
        try {
            RtmTokenBuilder token = new RtmTokenBuilder();
            return token.buildToken(appId, appCertificate, userId, RtmTokenBuilder.Role.Rtm_User, 0);
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }
}
