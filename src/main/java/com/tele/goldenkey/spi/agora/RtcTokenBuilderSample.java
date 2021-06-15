package com.tele.goldenkey.spi.agora;


import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;

/**
 * 视频通话
 */
public class RtcTokenBuilderSample {
    private final static String appId = "a75d7dfc56454049aa425f39b085db94";
    private final static String appCertificate = "a0a62266ac204e9eae02add460fabcbd";
    private final static int privilegeTs = 24 * 60 * 60;

    /**
     * @param groupId 房间id
     * @param userId  用户id
     * @param role    角色
     * @return
     */
    public static String buildRtcToken(String groupId, String userId, RtcTokenBuilder.Role role) {
        return new RtcTokenBuilder().buildTokenWithUserAccount(appId, appCertificate, groupId, userId, role, privilegeTs);
    }
}
