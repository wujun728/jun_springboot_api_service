package com.itboy.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SysRole
 * @Description 角色BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/26 0026 16:39
 **/
@Entity
@Table(name = "AUTH_ROLE")
@Data
public class SysRole implements Serializable {

    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer roleId; // 编号
    @Column(nullable = false, unique = true)
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String description; // 角色描述,UI界面显示使用
    private Boolean available = Boolean.TRUE; // 是否可用,如果不可用将不会添加给用户
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="AUTH_MENU",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;
}
