package com.jun.plugin.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_user1")
public class SysUser1 {
    /**
     * 用户id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 用户密码密文
     */
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 部门id
     */
    @Column(name = "dept_id")
    private String deptId;

    /**
     * 真实名称
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private Byte status;

    /**
     * 性别(1.男 2.女)
     */
    private Byte sex;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Byte deleted;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    @Column(name = "create_where")
    private Byte createWhere;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取用户id
     *
     * @return id - 用户id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户id
     *
     * @param id 用户id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取账户名称
     *
     * @return username - 账户名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置账户名称
     *
     * @param username 账户名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取加密盐值
     *
     * @return salt - 加密盐值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置加密盐值
     *
     * @param salt 加密盐值
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取用户密码密文
     *
     * @return password - 用户密码密文
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置用户密码密文
     *
     * @param password 用户密码密文
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号码
     *
     * @return phone - 手机号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号码
     *
     * @param phone 手机号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取部门id
     *
     * @return dept_id - 部门id
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 设置部门id
     *
     * @param deptId 部门id
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取真实名称
     *
     * @return real_name - 真实名称
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实名称
     *
     * @param realName 真实名称
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取邮箱(唯一)
     *
     * @return email - 邮箱(唯一)
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱(唯一)
     *
     * @param email 邮箱(唯一)
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取账户状态(1.正常 2.锁定 )
     *
     * @return status - 账户状态(1.正常 2.锁定 )
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置账户状态(1.正常 2.锁定 )
     *
     * @param status 账户状态(1.正常 2.锁定 )
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取性别(1.男 2.女)
     *
     * @return sex - 性别(1.男 2.女)
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * 设置性别(1.男 2.女)
     *
     * @param sex 性别(1.男 2.女)
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * 获取是否删除(1未删除；0已删除)
     *
     * @return deleted - 是否删除(1未删除；0已删除)
     */
    public Byte getDeleted() {
        return deleted;
    }

    /**
     * 设置是否删除(1未删除；0已删除)
     *
     * @param deleted 是否删除(1未删除；0已删除)
     */
    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    /**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取创建来源(1.web 2.android 3.ios )
     *
     * @return create_where - 创建来源(1.web 2.android 3.ios )
     */
    public Byte getCreateWhere() {
        return createWhere;
    }

    /**
     * 设置创建来源(1.web 2.android 3.ios )
     *
     * @param createWhere 创建来源(1.web 2.android 3.ios )
     */
    public void setCreateWhere(Byte createWhere) {
        this.createWhere = createWhere;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}