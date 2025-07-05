package com.myster.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 微信OpenID
     */
    @Column(name = "openid", unique = true, nullable = false, length = 100)
    private String openid;

    /**
     * 微信昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 255)
    private String avatar;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20)
    @Size(max = 20, message = "手机号长度不能超过20位")
    private String phone;

    /**
     * 密码（MD5加密）
     */
    @Column(name = "password", length = 32)
    private String password;

    /**
     * 性别：0-未知，1-男，2-女
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 用户状态：0-禁用，1-正常
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
} 