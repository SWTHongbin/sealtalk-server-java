package com.tele.goldenkey.spi.agora;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static com.tele.goldenkey.controller.LiveController.AGORA_CHANNEL_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgoraRecordingService {

    private final static String GET_RESOURCE_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/acquire";

    private final static String START_CLOUD_RECORDING_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/resourceid/%s/mode/mix/start";

    private final static String STOP_CLOUD_RECORDING_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/resourceid/%s/sid/%s/mode/mix/stop";

    private final static String CNAME_PREFIX = "tele_";

    private final RestTemplate restTemplate;

    private final RedissonClient redissonClient;


    /**
     * 开始录屏
     *
     * @param liveId
     * @param uId
     * @return
     */
    public String startRecording(String liveId, String uId) {
        String resourceId = getResourceId(liveId, uId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", RtmTokenBuilderSample.BASE_AUTHORIZATION);
        String token = RtcTokenBuilderSample.buildRtcToken(AGORA_CHANNEL_PREFIX + uId, uId, RtcTokenBuilder.Role.Role_Publisher);
        RecordeDto recordeDto = new RecordeDto(CNAME_PREFIX.concat(liveId), uId, new RecordeClient(token));
        log.info("======{}", JSON.toJSONString(recordeDto));
        HttpEntity<Object> httpEntity = new HttpEntity<>(recordeDto, headers);
        String body = restTemplate.exchange(String.format(START_CLOUD_RECORDING_URL, resourceId), HttpMethod.POST, httpEntity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(body);
        return json.getString("resourceId");
    }

    public String stopRecording(String liveId, String uId, String sid) {
        String resourceId = getResourceId(liveId, uId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", RtmTokenBuilderSample.BASE_AUTHORIZATION);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        String body = restTemplate.exchange(String.format(STOP_CLOUD_RECORDING_URL, resourceId, sid), HttpMethod.POST, httpEntity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(body);
        return json.getString("resourceId");
    }

    private String getResourceId(String liveId, String uId) {
        RMapCache<String, String> rMapCache = redissonClient.getMapCache("resourceId");
        String key = liveId.concat(uId), resourceId = rMapCache.get(key);
        if (StringUtils.isNotEmpty(resourceId)) return resourceId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", RtmTokenBuilderSample.BASE_AUTHORIZATION);
        HttpEntity<Object> httpEntity = new HttpEntity<>(new Acquire(CNAME_PREFIX.concat(liveId), uId), headers);
        String body = restTemplate.exchange(GET_RESOURCE_URL, HttpMethod.POST, httpEntity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(body);
        resourceId = json.getString("resourceId");
        rMapCache.put(key, resourceId, 4, TimeUnit.MINUTES);
        return resourceId;
    }

    @Data
    private static class Acquire {

        private String cname;

        private String uid;

        private Request clientRequest = new Request();

        public Acquire(String cname, String uid) {
            this.cname = cname;
            this.uid = uid;
        }

        @Data
        public static class Request {
            private Integer resourceExpiredHour = 24;
        }
    }

    @Data
    @AllArgsConstructor
    private static class RecordeDto {

        private String cname;

        private String uid;

        private RecordeClient clientRequest;

    }

    @Data
    private static class RecordeClient {
        public RecordeClient(String token) {
            this.token = token;
        }

        private String token;

        private RecordeConfig recordingConfig = new RecordeConfig();

        private StorageDto storageConfig = new StorageDto();
    }

    @Data
    private static class RecordeConfig {

        private String streamTypes = "2";
    }


    @Data
    private static class StorageDto {

        private String accessKey = "IDWmE1z9uVy0Svg8PyrW9w8ebshSTzMU40QXIdVk";

        private String region = "3";

        private String bucket = "tele-live";

        private String secretKey = "-sBKxJdD-t1jq7qEtZdfX2pbvLOfnvORJ5MQXJGl";

        private String vendor = "0";

        private String[] fileNamePrefix = new String[]{"agora", "recorde"};
    }
}
