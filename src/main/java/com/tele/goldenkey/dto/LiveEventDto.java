package com.tele.goldenkey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiveEventDto {

    /**
     * livedId
     */
    private Long livedId;

    /**
     * is who
     */
    private Integer fromUserId;
    /**
     * to  who
     */
    private Integer toTerminalId;
}
