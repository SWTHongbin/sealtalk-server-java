package com.tele.goldenkey.dto;

import lombok.Data;

@Data
public class LiveUserDto {

    private Integer userId;

    private String phone;

    private String portraitUri;

    private Integer maiStatus;

    private String name;
}
