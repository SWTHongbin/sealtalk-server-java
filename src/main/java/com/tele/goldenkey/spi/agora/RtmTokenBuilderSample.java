package com.tele.goldenkey.spi.agora;


import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import com.tele.goldenkey.spi.agora.rtm.RtmTokenBuilder;
import com.tele.goldenkey.util.SpringContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 云信令 token
 */
@Slf4j
public class RtmTokenBuilderSample {
    private final static String APP_ID = "a75d7dfc56454049aa425f39b085db94";
    private final static String APP_CERTIFICATE = "a0a62266ac204e9eae02add460fabcbd";
    private final static String USER_ID = "825034474290024448";
    private final static String CHANNEL_REQUEST_URL = "https://api.agora.io/dev/v2/project/" + APP_ID + "/rtm/users/%s/channel_messages";
    private final static String TERMINAL_REQUEST_URL = "https://api.agora.io/dev/v2/project/" + APP_ID + "/rtm/users/%s/peer_messages";
    private final static RedissonClient redissonClient = SpringContextUtil.getBean(RedissonClient.class);


    /**
     * @param userId 用户id
     * @return
     */
    public static String buildRtmToken(String userId) {
        String token = null;
        try {
            RMapCache<String, String> mapCache = redissonClient.getMapCache("tele.goldenkey:agora:rtm");
            token = mapCache.get(userId);
            if (StringUtils.isNotBlank(token)) return token;
            RtmTokenBuilder tokenBuilder = new RtmTokenBuilder();
            token = tokenBuilder.buildToken(APP_ID, APP_CERTIFICATE, userId, RtmTokenBuilder.Role.Rtm_User, 0);
            mapCache.putAsync(userId, token, 23, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("rtm token 失败:", e);
        }
        return token;
    }

    /**
     * 频道消息推送   管理员发送
     *
     * @param channelName
     * @param rtmMsgDto
     */
    public static void sendMsgOfChannel(String channelName, RtmMsgDto rtmMsgDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-agora-token", buildRtmToken(USER_ID));
        headers.set("x-agora-uid", USER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<>(new RtmChannelBody(channelName, JSON.toJSONString(rtmMsgDto)), headers);
        RestTemplate restTemplate = SpringContextUtil.getBean(RestTemplate.class);
        log.info(restTemplate.exchange(String.format(CHANNEL_REQUEST_URL, USER_ID), HttpMethod.POST, httpEntity, String.class).getBody());
    }

    /**
     * 终端消息发送
     *
     * @param userId     是谁发
     * @param terminalId 给谁发
     * @param rtmMsgDto  消息
     */
    public static void sendMsgOfTerminal(String userId, String terminalId, RtmMsgDto rtmMsgDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-agora-token", buildRtmToken(userId));
        headers.set("x-agora-uid", userId);
        HttpEntity<Object> httpEntity = new HttpEntity<>(new RtmTerminalBody(terminalId, JSON.toJSONString(rtmMsgDto)), headers);
        RestTemplate restTemplate = SpringContextUtil.getBean(RestTemplate.class);
        log.info(restTemplate.exchange(String.format(TERMINAL_REQUEST_URL, userId), HttpMethod.POST, httpEntity, String.class).getBody());
    }

    @Data
    static class RtmChannelBody {
        private String channel_name;
        private Boolean enable_historical_messaging = false;
        private String payload;

        public RtmChannelBody(String channel_name, String payload) {
            this.channel_name = channel_name;
            this.payload = payload;
        }
    }

    @Data
    static class RtmTerminalBody {
        private String destination;
        private Boolean enable_offline_messaging = true;
        private Boolean enable_historical_messaging = false;
        private String payload;

        public RtmTerminalBody(String destination, String payload) {
            this.destination = destination;
            this.payload = payload;
        }
    }
}
