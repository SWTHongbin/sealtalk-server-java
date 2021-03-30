package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "live_statuses")
public class LiveStatuses implements Serializable {

    @Id
    @Column(name = "liveId")
    private Integer liveId;

    @Column(name = "pushUrl")
    private String pushUrl;

    @Column(name = "liveUrl")
    private String liveUrl;

    private Integer status;

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