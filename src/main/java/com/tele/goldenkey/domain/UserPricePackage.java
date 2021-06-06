package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "user_price_package")
public class UserPricePackage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId")
    private Integer userId;


    /**
     * 音频剩余
     */
    @Column(name = "audioBalance")
    private BigDecimal audioBalance;

    /**
     * 视频剩余
     */
    @Column(name = "videoBalance")
    private BigDecimal videoBalance;


    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;


    private static final long serialVersionUID = 1L;
}