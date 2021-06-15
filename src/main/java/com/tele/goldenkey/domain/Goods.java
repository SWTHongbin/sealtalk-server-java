package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "goods")
public class Goods implements Serializable {

    private Long id;

    private Integer userId;

    private String name;

    private String goodsLink;

    private String description;

    private String pictureLink;

    private Date createdAt;

    private Date updatedAt;
}
