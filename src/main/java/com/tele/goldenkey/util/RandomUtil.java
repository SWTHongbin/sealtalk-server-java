package com.tele.goldenkey.util;

import java.util.Random;

/**
 * @Author: xiuwei.nie
 * @Date: 2020/7/7
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
public class RandomUtil {

    private static final Random random = new Random();

    /**
     * 生成随机数，区间[min,max]
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomBetween(Integer min, Integer max) {
        return random.nextInt(max - min + 1) + min;
    }
}
