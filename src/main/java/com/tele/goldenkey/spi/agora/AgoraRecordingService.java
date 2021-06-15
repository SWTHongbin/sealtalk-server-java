package com.tele.goldenkey.spi.agora;

import com.alibaba.fastjson.JSONObject;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.RandomUtil;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static com.tele.goldenkey.controller.LiveController.AGORA_CHANNEL_PREFIX;


/**
 * 云 录制
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgoraRecordingService {

    private final static String GET_RESOURCE_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/acquire";

    private final static String START_CLOUD_RECORDING_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/resourceid/%s/mode/mix/start";

    private final static String STOP_CLOUD_RECORDING_URL = "https://api.agora.io/v1/apps/a75d7dfc56454049aa425f39b085db94/cloud_recording/resourceid/%s/sid/%s/mode/mix/stop";

    private final RestTemplate restTemplate;

    private final RedissonClient redissonClient;


    public void startRecording(String liveId) throws ServiceException {
        String uId = liveId + RandomUtil.randomBetween(10000, 99999);
        liveId = AGORA_CHANNEL_PREFIX.concat(liveId);

        String resourceId = getResourceId(liveId, uId);
        ValidateUtils.notNull(resourceId);

        String token = RtcTokenBuilderSample.buildRtcToken(liveId, uId, RtcTokenBuilder.Role.Role_Subscriber);
        HttpEntity<Object> httpEntity = new HttpEntity<>(initStartRecordParam(liveId, uId, token), getHttpBaseHeader());
        String url = String.format(START_CLOUD_RECORDING_URL, resourceId);
        String body = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();
        log.info(" liveId:{},uId:{}-start recording  result:{}", liveId, uId, body);

        RecordDto recordDto = JSONObject.parseObject(body, RecordDto.class);
        ValidateUtils.isTrue(recordDto != null && StringUtils.isNotEmpty(recordDto.getSid()));
        recordDto.setUId(uId);
        redissonClient.getBucket("recording_" + liveId).set(recordDto, 2, TimeUnit.DAYS);
    }

    public String stopRecording(String liveId) throws ServiceException {
        liveId = AGORA_CHANNEL_PREFIX.concat(liveId);
        RBucket<RecordDto> bucket = redissonClient.getBucket("recording_" + liveId);
        ValidateUtils.isTrue(bucket.isExists());
        RecordDto recordDto = bucket.get();
        bucket.deleteAsync();
        HttpEntity<Acquire> httpEntity = new HttpEntity<>(new Acquire(liveId, recordDto.getUId(), new Acquire.Request()), getHttpBaseHeader());
        String url = String.format(STOP_CLOUD_RECORDING_URL, recordDto.getResourceId(), recordDto.getSid());
        String body = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();
        log.info(" liveId:{},uId:{}-stop recordeDto :{},result:{}", liveId, recordDto.getUId(), recordDto, body);
        JSONObject serverResponse = JSONObject.parseObject(body).getJSONObject("serverResponse");
        return serverResponse != null ? serverResponse.getString("fileList") : null;
    }

    private String getResourceId(String liveId, String uId) {
        RMapCache<String, String> rMapCache = redissonClient.getMapCache("resourceId");
        String key = liveId.concat(uId), resourceId = rMapCache.get(key);
        if (StringUtils.isNotEmpty(resourceId)) return resourceId;

        HttpEntity<Object> httpEntity = new HttpEntity<>(new Acquire(liveId, uId), getHttpBaseHeader());
        String body = restTemplate.exchange(GET_RESOURCE_URL, HttpMethod.POST, httpEntity, String.class).getBody();
        JSONObject json = JSONObject.parseObject(body);
        resourceId = json.getString("resourceId");
        rMapCache.putAsync(key, resourceId, 4, TimeUnit.MINUTES);
        return resourceId;
    }

    private HttpHeaders getHttpBaseHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        headers.set("Authorization", RtmTokenBuilderSample.BASE_AUTHORIZATION);
        return headers;
    }

    @Data
    private static class RecordDto implements Serializable {

        private String resourceId;

        private String sid;

        private String uId;
    }

    @Data
    @AllArgsConstructor
    private static class Acquire {

        private String cname;

        private String uid;

        private Request clientRequest = new Request("NA", 24, 0);

        public Acquire(String cname, String uid) {
            this.cname = cname;
            this.uid = uid;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Request {
            private String region;
            private Integer resourceExpiredHour;
            private Integer scene;
        }

    }

    private String initStartRecordParam(String liveId, String uId, String token) {
        return "{" +
                "    \"uid\": \"" + uId + "\"," +
                "    \"cname\": \"" + liveId + "\"," +
                "\"clientRequest\": {" +
                "        \"token\": \"" + token + "\"," +
                "        \"recordingConfig\": {" +
                "            \"maxIdleTime\": 30," +
                "            \"streamTypes\": 2," +
                "            \"audioProfile\": 1," +
                "            \"channelType\": 0, " +
                "            \"videoStreamType\": 1, " +
                "            \"transcodingConfig\": {" +
                "                \"height\": 640, " +
                "                \"width\": 360," +
                "                \"bitrate\": 500, " +
                "                \"fps\": 15, " +
                "                \"mixedVideoLayout\": 1," +
                "                \"backgroundColor\": \"#FF0000\"" +
                "                        }," +
                "            \"subscribeVideoUids\": [\"123\",\"456\"], " +
                "            \"subscribeAudioUids\": [\"123\",\"456\"]," +
                "            \"subscribeUidGroup\": 0" +
                "       }, " +
                "        \"storageConfig\": {" +
                "            \"accessKey\": \"IDWmE1z9uVy0Svg8PyrW9w8ebshSTzMU40QXIdVk\"," +
                "            \"region\": 3," +
                "            \"bucket\": \"tele-live\"," +
                "            \"secretKey\": \"-sBKxJdD-t1jq7qEtZdfX2pbvLOfnvORJ5MQXJGl\"," +
                "            \"vendor\": 0, " +
                "            \"fileNamePrefix\": [\"agora\",\"recorde\"]" +
                "       }" +
                "   }" +
                "}";
    }
}
