package com.itboy.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName SysUser
 * @Description 用户BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/26 0026 16:37
 **/
@Entity
@Table(name = "AUTH_USER")
@Data
public class SysUser implements Serializable {

    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long userId;
    @Column(nullable = false, unique = true)
    private String userName; //登录用户名
    @Column(nullable = false)
    private String name;//名称（昵称或者真实姓名，根据实际情况定义）
    @Column(nullable = false)
    private String password;
    private String salt;//加密密码的盐
    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "AUTH_ROLE", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;//创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;//过期日期
    private String email;
    @Column
    private String credentialsSalt;
}
