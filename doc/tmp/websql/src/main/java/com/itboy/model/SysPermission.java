package com.itboy.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName SysPermission
 * @Description 菜单BEAN  可实现控制更细化权限
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/26 0026 16:40
 **/
@Entity
@Table(name = "AUTH_MENU")
@Data
public class SysPermission implements Serializable {

    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer permissionId;//主键.
    @Column(nullable = false)
    private String permissionName;//名称.
    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Boolean available = Boolean.TRUE;
}
