package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

public class Company {
    /**
     * 公司自动ID
     */
	@Id
    @Column(name = "COMPANY_ID")
    private Integer companyId;

    /**
     * 公司名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 公司电话
     */
    @Column(name = "TEL")
    private String tel;

    /**
     * 公司传真
     */
    @Column(name = "FAX")
    private String fax;

    /**
     * 公司地址
     */
    @Column(name = "ADDRESS")
    private String address;

    /**
     * 邮政编码
     */
    @Column(name = "ZIP")
    private String zip;

    /**
     * 公司邮件地址
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 公司联络人
     */
    @Column(name = "CONTACT")
    private String contact;

    /**
     * 状态
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 创造日期
     */
    @Column(name = "CREATED")
    private Date created;

    /**
     * 修改日期
     */
    @Column(name = "LASTMOD")
    private Date lastmod;

    /**
     * 公司负责人
     */
    @Column(name = "MANAGER")
    private String manager;

    /**
     * 开户行
     */
    @Column(name = "BANK")
    private String bank;

    /**
     * 银行账号
     */
    @Column(name = "BANKACCOUNT")
    private String bankaccount;

    /**
     * 备注
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 创建人
     */
    @Column(name = "CREATER")
    private Integer creater;

    /**
     * 修改人
     */
    @Column(name = "MODIFYER")
    private Integer modifyer;

    /**
     * 获取公司自动ID
     *
     * @return COMPANY_ID - 公司自动ID
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * 设置公司自动ID
     *
     * @param companyId 公司自动ID
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取公司名称
     *
     * @return NAME - 公司名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置公司名称
     *
     * @param name 公司名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取公司电话
     *
     * @return TEL - 公司电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置公司电话
     *
     * @param tel 公司电话
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取公司传真
     *
     * @return FAX - 公司传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置公司传真
     *
     * @param fax 公司传真
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 获取公司地址
     *
     * @return ADDRESS - 公司地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置公司地址
     *
     * @param address 公司地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取邮政编码
     *
     * @return ZIP - 邮政编码
     */
    public String getZip() {
        return zip;
    }

    /**
     * 设置邮政编码
     *
     * @param zip 邮政编码
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * 获取公司邮件地址
     *
     * @return EMAIL - 公司邮件地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置公司邮件地址
     *
     * @param email 公司邮件地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取公司联络人
     *
     * @return CONTACT - 公司联络人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置公司联络人
     *
     * @param contact 公司联络人
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取状态
     *
     * @return STATUS - 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取创造日期
     *
     * @return CREATED - 创造日期
     */
    public Date getCreated() {
        return created;
    }

    /**
     * 设置创造日期
     *
     * @param created 创造日期
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * 获取修改日期
     *
     * @return LASTMOD - 修改日期
     */
    public Date getLastmod() {
        return lastmod;
    }

    /**
     * 设置修改日期
     *
     * @param lastmod 修改日期
     */
    public void setLastmod(Date lastmod) {
        this.lastmod = lastmod;
    }

    /**
     * 获取公司负责人
     *
     * @return MANAGER - 公司负责人
     */
    public String getManager() {
        return manager;
    }

    /**
     * 设置公司负责人
     *
     * @param manager 公司负责人
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * 获取开户行
     *
     * @return BANK - 开户行
     */
    public String getBank() {
        return bank;
    }

    /**
     * 设置开户行
     *
     * @param bank 开户行
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取银行账号
     *
     * @return BANKACCOUNT - 银行账号
     */
    public String getBankaccount() {
        return bankaccount;
    }

    /**
     * 设置银行账号
     *
     * @param bankaccount 银行账号
     */
    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    /**
     * 获取备注
     *
     * @return DESCRIPTION - 备注
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置备注
     *
     * @param description 备注
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取创建人
     *
     * @return CREATER - 创建人
     */
    public Integer getCreater() {
        return creater;
    }

    /**
     * 设置创建人
     *
     * @param creater 创建人
     */
    public void setCreater(Integer creater) {
        this.creater = creater;
    }

    /**
     * 获取修改人
     *
     * @return MODIFYER - 修改人
     */
    public Integer getModifyer() {
        return modifyer;
    }

    /**
     * 设置修改人
     *
     * @param modifyer 修改人
     */
    public void setModifyer(Integer modifyer) {
        this.modifyer = modifyer;
    }
}