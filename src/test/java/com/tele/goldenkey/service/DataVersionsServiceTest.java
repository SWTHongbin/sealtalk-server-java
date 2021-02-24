package com.tele.goldenkey.service;

import com.tele.goldenkey.domain.DataVersions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/8/5
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataVersionsServiceTest {

    @Autowired
    private DataVersionsService dataVersionsService;

    @Test
    public void createDataVersion() {

        DataVersions dataVersions = new DataVersions();
        dataVersions.setUserId(1);
        dataVersionsService.saveSelective(dataVersions);
    }
}