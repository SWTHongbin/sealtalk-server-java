package com.tele.goldenkey.live;

import com.tele.goldenkey.util.SpringContextUtil;

import java.util.HashMap;
import java.util.Map;

public class LiveEventFactory {

    private final static Map<String, LiveEventCls> hashMap = new HashMap<>();

    public static LiveEventCls getByKey(String key) {
        if (hashMap.isEmpty()) {
            synchronized (hashMap) {
                SpringContextUtil.getApplicationContext().getBeansOfType(LiveEventCls.class).forEach((k, v) -> hashMap.put(v.getEventName(), v));
            }
        }
        return hashMap.get(key);
    }
}
