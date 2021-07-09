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
     * 录制  1 是  0 否
     */
    private Integer recorde;

    /**
     * 主题
     */
    private String theme;

    /**
     * 录制url
     */
    private String recordUrl;

    /**
     * 直播方式
     *
     * @see com.tele.goldenkey.enums.SkuType
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

    @Column(name = "pingTime")
    private Date pingTime;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @Column(name = "code")
    private String code;

    private static final long serialVersionUID = 1L;
}