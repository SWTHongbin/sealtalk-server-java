package com.tele.goldenkey.spi.agora.dto;

import lombok.Data;

@Data
public class RtmMsgDto<T> {

    private Integer code;

    private String message;

    private T data;
}
