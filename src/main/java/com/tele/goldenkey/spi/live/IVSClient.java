package com.tele.goldenkey.spi.live;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;
import software.amazon.awssdk.services.ivs.model.*;

@Slf4j
@Component
public class IVSClient {

    private final static IvsClient IVS_CLIENT = IvsClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(SystemPropertyCredentialsProvider.create())
            .build();

    public Channel createChannel(String channelName) {
        CreateChannelRequest build = CreateChannelRequest.builder()
                .name(channelName)
                .build();
        CreateChannelResponse channelResponse = IVS_CLIENT.createChannel(build);
        SdkHttpResponse sdkHttpResponse = channelResponse.sdkHttpResponse();
        if (!sdkHttpResponse.isSuccessful()) {
            return null;
        }
        return channelResponse.channel();
    }

    public Channel getChannel(String arn) {
        GetChannelRequest build = GetChannelRequest.builder()
                .arn(arn)
                .build();
        GetChannelResponse channelResponse = IVS_CLIENT.getChannel(build);
        SdkHttpResponse sdkHttpResponse = channelResponse.sdkHttpResponse();
        if (!sdkHttpResponse.isSuccessful()) {
            return null;
        }
        return channelResponse.channel();
    }


    public Boolean stopStream(String arn) {
        try {
            StopStreamRequest request = StopStreamRequest.builder()
                    .channelArn(arn)
                    .build();
            return IVS_CLIENT.stopStream(request).sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            log.error("stop stream err", e);
        }
        return false;
    }


    public Boolean removeChannel(String arn) {
        delStreamKey(arn);
        return delChannel(arn);
    }

    private static Boolean delStreamKey(String arn) {
        try {
            DeleteStreamKeyRequest request = DeleteStreamKeyRequest.builder()
                    .arn(arn)
                    .build();
            return IVS_CLIENT.deleteStreamKey(request).sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            log.error("del stream err", e);
            return false;
        }
    }

    private static Boolean delChannel(String arn) {
        DeleteChannelRequest request = DeleteChannelRequest.builder()
                .arn(arn)
                .build();
        return IVS_CLIENT.deleteChannel(request).sdkHttpResponse().isSuccessful();
    }
}
