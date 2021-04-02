package com.tele.goldenkey.spi.live;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ivs.IvsClient;
import software.amazon.awssdk.services.ivs.model.Channel;
import software.amazon.awssdk.services.ivs.model.CreateChannelResponse;
import software.amazon.awssdk.services.ivs.model.GetChannelResponse;

@Slf4j
@Component
public class IVSClient {

    private final static IvsClient IVS_CLIENT = IvsClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(SystemPropertyCredentialsProvider.create())
            .build();

    public Channel createChannel(String channelName) {
        CreateChannelResponse channelResponse = IVS_CLIENT.createChannel((builder -> builder.name(channelName)));
        SdkHttpResponse sdkHttpResponse = channelResponse.sdkHttpResponse();
        if (!sdkHttpResponse.isSuccessful()) {
            return null;
        }
        return channelResponse.channel();
    }

    public Channel getChannel(String arn) {
        GetChannelResponse channelResponse = IVS_CLIENT.getChannel((request) -> request.arn(arn));
        SdkHttpResponse sdkHttpResponse = channelResponse.sdkHttpResponse();
        if (!sdkHttpResponse.isSuccessful()) {
            return null;
        }
        return channelResponse.channel();
    }


    public Boolean stopStream(String arn) {
        try {
            return IVS_CLIENT.stopStream((request) -> request.channelArn(arn)).sdkHttpResponse().isSuccessful();
        } catch (Exception ignored) {
        }
        return false;
    }


    public Boolean removeChannel(String arn) {
        delStreamKey(arn);
        return delChannel(arn);
    }

    private static Boolean delStreamKey(String arn) {
        try {
            return IVS_CLIENT.deleteStreamKey((request) -> request.arn(arn)).sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    private static Boolean delChannel(String arn) {
        return IVS_CLIENT.deleteChannel((request) -> request.arn(arn)).sdkHttpResponse().isSuccessful();
    }
}
