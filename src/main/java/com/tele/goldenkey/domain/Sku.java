package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "sku")
public class Sku implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "userId")
    private Integer userId;


    /**
     * sku 类型
     */
    private Integer type;

    /**
     * 剩余套餐
     */
    private BigDecimal surplus;


    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;


    private static final long serialVersionUID = 1L;
}