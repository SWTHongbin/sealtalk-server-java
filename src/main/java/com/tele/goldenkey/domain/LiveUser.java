package com.tele.goldenkey.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "live_user")
public class LiveUser implements Serializable {

    @Id
    private Integer id;

    @Column(name = "liveId")
    private Integer liveId;

    @Column(name = "userId")
    private Integer userId;

    private String phone;

    @Column(name = "portraitUri")
    private String portraitUri;

    private String name;

    /**
     * 买状态 1 开麦 0 关麦
     */
    @Column(name = "maiStatus")
    private Integer maiStatus;

    /**
     * 是否允许发言 1 开 0 关
     */
    @Column(name = "permissionSpeak")
    private Integer permissionSpeak;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;
}
