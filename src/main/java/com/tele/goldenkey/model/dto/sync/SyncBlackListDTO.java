package com.tele.goldenkey.model.dto.sync;

import lombok.Data;

/**
 * @Author: Jianlu.Yu
 * @Date: 2020/9/3
 * @Description:
 * @Copyright (c) 2020, rongcloud.cn All Rights Reserved
 */
@Data
public class SyncBlackListDTO {
    private String friendId;
    private Boolean status;
    private Long timestamp;

    private SyncUserDTO user;



}