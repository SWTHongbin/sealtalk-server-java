package com.tele.goldenkey.spi.live.impl;

import com.tele.goldenkey.spi.live.LiveSpiService;
import org.springframework.stereotype.Component;

@Component
public class QiNiuLiveSpiServiceImpl implements LiveSpiService {
    /**
     * todo
     *
     * @param key
     * @return
     */
    @Override
    public String pushUrl(String key) {
        return "www.baidu.com";
    }

    @Override
    public String liveUrl(String key) {
        return "www.baidu.com";
    }
}
