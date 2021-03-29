package com.tele.goldenkey.spi.agora;


import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;

/**
 * 视频通话
 */
public class RtcTokenBuilderSample {
    private final static String appId = "a75d7dfc56454049aa425f39b085db94";
    private final static String appCertificate = "a0a62266ac204e9eae02add460fabcbd";

    public static String buildRtcToken(String groupId, String userId, RtcTokenBuilder.Role role) {
        return new RtcTokenBuilder()
                .buildTokenWithUserAccount(appId, appCertificate,
                        groupId, userId, role, 0);
    }
}
