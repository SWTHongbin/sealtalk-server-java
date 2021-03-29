package com.tele.goldenkey.spi.agora;


import com.alibaba.fastjson.JSON;
import com.tele.goldenkey.spi.agora.dto.RtmMsgDto;
import com.tele.goldenkey.spi.agora.rtm.RtmTokenBuilder;
import com.tele.goldenkey.util.SpringContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * 云信令 token
 */
@Slf4j
public class RtmTokenBuilderSample {
    private final static String APP_ID = "a75d7dfc56454049aa425f39b085db94";
    private final static String APP_CERTIFICATE = "a0a62266ac204e9eae02add460fabcbd";
    private final static String USER_ID = "825034474290024448";
    private final static String REQUEST_URL = "https://api.agora.io/dev/v2/project/" + APP_ID + "/rtm/users/%s/channel_messages";


    public static String buildRtmToken(String userId) {
        try {
            RtmTokenBuilder token = new RtmTokenBuilder();
            return token.buildToken(APP_ID, APP_CERTIFICATE, userId, RtmTokenBuilder.Role.Rtm_User, 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static void sendMsg(String channelName, RtmMsgDto rtmMsgDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-agora-token", buildRtmToken(USER_ID));
        headers.set("x-agora-uid", USER_ID);
        HttpEntity<Object> httpEntity = new HttpEntity<>(new RtmMsgBody(channelName, JSON.toJSONString(rtmMsgDto)), headers);
        RestTemplate restTemplate = SpringContextUtil.getBean(RestTemplate.class);
        log.info(restTemplate.exchange(String.format(REQUEST_URL, USER_ID), HttpMethod.POST, httpEntity, String.class).getBody());
    }

    @Data
    static class RtmMsgBody {
        private String channel_name;
        private Boolean enable_historical_messaging = false;
        private String payload;

        public RtmMsgBody(String channel_name, String payload) {
            this.channel_name = channel_name;
            this.payload = payload;
        }
    }

}
