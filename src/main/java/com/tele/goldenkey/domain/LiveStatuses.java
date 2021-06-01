package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "live_statuses")
public class LiveStatuses implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liveId")
    private Long liveId;

    @Column(name = "pushUrl")
    private String pushUrl;

    @Column(name = "liveUrl")
    private String liveUrl;

    @Column(name = "streamKey")
    private String streamKey;

    /**
     * 1 开播  0未开播
     */
    private Integer status;

    /**
     * 房主id
     */
    @Column(name = "anchorId")
    private Integer anchorId;

    /**
     * 封面url
     */
    @Column(name = "fmLink")
    private String fmLink;

    /**
     * 商品信息
     */
    @Column(name = "goods")
    private String goods;

    /**
     * 主题
     */
    private String theme;

    /**
     * 直播方式
     */
    private Integer type;

    /**
     * 是否允许连麦
     */
    @Column(name = "linkMai")
    private Integer linkMai;

    /**
     * 开始时间
     */
    @Column(name = "startTime")
    private Date startTime;

    @Column(name = "adminMaiStatus")
    private Integer adminMaiStatus;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "code")
    private String code;

    private static final long serialVersionUID = 1L;
}