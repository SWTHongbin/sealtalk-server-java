package com.tele.goldenkey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LiveEventDto {

    private Integer livedId;

    private Integer fromUserId;

    private Integer toTerminalId;
}
