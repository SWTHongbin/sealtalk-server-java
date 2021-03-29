package com.tele.goldenkey.spi.agora;


import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 视频通话
 */
public class RtcTokenBuilderSample {
    private final static String appId = "a75d7dfc56454049aa425f39b085db94";
    private final static String appCertificate = "a0a62266ac204e9eae02add460fabcbd";
    private final static RedissonClient redissonClient = SpringContextUtil.getBean(RedissonClient.class);

    public static String buildToken(String groupId, String userId, RtcTokenBuilder.Role role) {
        String key = groupId + "_" + userId;
        RMapCache<String, String> mapCache = redissonClient.getMapCache("tele.goldenkey:agora:rtc");
        String token = mapCache.get(key);
        if (StringUtils.isNotBlank(token)) return token;

        RtcTokenBuilder tokenBuilder = new RtcTokenBuilder();
        token = tokenBuilder.buildTokenWithUserAccount(appId, appCertificate,
                groupId, userId, role, 0);
        mapCache.put(key, token, 23, TimeUnit.HOURS);
        return token;
    }
}
