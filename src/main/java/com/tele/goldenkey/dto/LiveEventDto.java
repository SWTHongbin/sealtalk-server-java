package com.tele.goldenkey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiveEventDto {


    /**
     * 目前  房间id =userID
     */
    private Integer livedId;

    /**
     * 终端id
     */
    private Integer terminalId;


}
