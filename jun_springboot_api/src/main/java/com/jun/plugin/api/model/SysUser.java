package com.jun.plugin.api.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_user")
public class SysUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userName")
    private String username;

    @Column(name = "accountName")
    private String accountname;

    private String password;

    @Column(name = "credentialsSalt")
    private String credentialssalt;

    private String description;

    private String locked;

    @Column(name = "createTime")
    private Date createtime;

    /**
     * 逻辑删除状态0:存在1:删除
     */
    private Integer deletestatus;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return userName
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return accountName
     */
    public String getAccountname() {
        return accountname;
    }

    /**
     * @param accountname
     */
    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return credentialsSalt
     */
    public String getCredentialssalt() {
        return credentialssalt;
    }

    /**
     * @param credentialssalt
     */
    public void setCredentialssalt(String credentialssalt) {
        this.credentialssalt = credentialssalt;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return locked
     */
    public String getLocked() {
        return locked;
    }

    /**
     * @param locked
     */
    public void setLocked(String locked) {
        this.locked = locked;
    }

    /**
     * @return createTime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取逻辑删除状态0:存在1:删除
     *
     * @return deletestatus - 逻辑删除状态0:存在1:删除
     */
    public Integer getDeletestatus() {
        return deletestatus;
    }

    /**
     * 设置逻辑删除状态0:存在1:删除
     *
     * @param deletestatus 逻辑删除状态0:存在1:删除
     */
    public void setDeletestatus(Integer deletestatus) {
        this.deletestatus = deletestatus;
    }
}