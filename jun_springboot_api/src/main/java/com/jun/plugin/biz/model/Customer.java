package com.jun.plugin.biz.model;

import java.util.Date;
import javax.persistence.*;

public class Customer {
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MYID")
    private String myid;

    /**
     * A:正常,I:删除
     */
    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "LASTMOD")
    private Date lastmod;

    /**
     * 创建人
     */
    @Column(name = "CREATER")
    private Integer creater;

    /**
     * 修改人
     */
    @Column(name = "MODIFIYER")
    private Integer modifiyer;

    /**
     * 客户类型
     */
    @Column(name = "CLASS_ID")
    private Integer classId;

    @Column(name = "TEL")
    private String tel;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "URL")
    private String url;

    @Column(name = "EMAIL")
    private String email;

    /**
     * 区域编码
     */
    @Column(name = "AREA_ID")
    private Integer areaId;

    /**
     * 区域名称
     */
    @Column(name = "AREA_NAME")
    private String areaName;

    /**
     * 省份编码
     */
    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    /**
     * 省份名称
     */
    @Column(name = "PROVINCE_NAME")
    private String provinceName;

    /**
     * 城市编码
     */
    @Column(name = "CITY_ID")
    private Integer cityId;

    /**
     * 城市名称
     */
    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ZIP")
    private String zip;

    /**
     * 客户等级编码
     */
    @Column(name = "LEVEL_ID")
    private Integer levelId;

    /**
     * 客户等级名称
     */
    @Column(name = "LEVEL_NAME")
    private String levelName;

    /**
     * 价格等级
     */
    @Column(name = "PRICE_LEVEL")
    private Integer priceLevel;

    /**
     * 联系周期
     */
    @Column(name = "CONTACT_PERIOD")
    private Integer contactPeriod;

    /**
     * 客户来源编码
     */
    @Column(name = "SOURCE_ID")
    private Integer sourceId;

    /**
     * 客户来源名称
     */
    @Column(name = "SOURCE_NAME")
    private String sourceName;

    /**
     * 客户公司性质编码
     */
    @Column(name = "NATURE_ID")
    private Integer natureId;

    /**
     * 客户公司性质名称
     */
    @Column(name = "NATURE_NAME")
    private String natureName;

    /**
     * 行业编码
     */
    @Column(name = "INDUSTRY_ID")
    private Integer industryId;

    /**
     * 行业名称
     */
    @Column(name = "INDUSTRY_NAME")
    private String industryName;

    /**
     * 主业务业
     */
    @Column(name = "MAIN_BUSINESS")
    private String mainBusiness;

    /**
     * 公司规模编码
     */
    @Column(name = "SIZE_ID")
    private Integer sizeId;

    /**
     * 公司规模
     */
    @Column(name = "SIZE_NAME")
    private String sizeName;

    /**
     * 开业年份
     */
    @Column(name = "SETUP_DATE")
    private Date setupDate;

    /**
     * 注册资金
     */
    @Column(name = "CAPITAL")
    private Integer capital;

    /**
     * 法人
     */
    @Column(name = "CORPORATION")
    private String corporation;

    /**
     * 信用编码
     */
    @Column(name = "CREDIT_ID")
    private Integer creditId;

    /**
     * 信用名称
     */
    @Column(name = "CREDIT_NAME")
    private String creditName;

    @Column(name = "BANK")
    private String bank;

    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 税号
     */
    @Column(name = "TAXNO")
    private String taxno;

    /**
     * 共享人(以逗号分隔)
     */
    @Column(name = "SHARED")
    private String shared;

    /**
     * 客户父项(不用)
     */
    @Column(name = "PID")
    private Integer pid;

    /**
     * 附件编码
     */
    @Column(name = "ATTACHMENT_ID")
    private Integer attachmentId;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 销售编码
     */
    @Column(name = "SALE_ID")
    private Integer saleId;

    /**
     * 销售名称
     */
    @Column(name = "SALE_NAME")
    private String saleName;

    /**
     * 销售组织编码
     */
    @Column(name = "SALE_ORGANIZATION_ID")
    private Integer saleOrganizationId;

    /**
     * 销售组织名称
     */
    @Column(name = "SALE_ORGANIZATION_NAME")
    private String saleOrganizationName;

    /**
     * 客户状态 T:交易中，S:禁用
     */
    @Column(name = "CUSTOMER_STATUS")
    private String customerStatus;

    /**
     * 客户类型名称
     */
    @Column(name = "CLASS_NAME")
    private String className;

    @Column(name = "EMP_COUNT")
    private Integer empCount;

    /**
     * 税率
     */
    @Column(name = "TAX")
    private Integer tax;

    /**
     * 立帐方式 1:出库立账,2:开票立帐,3:不立账(不用)
     */
    @Column(name = "SETUP_ACCOUNT")
    private Integer setupAccount;

    /**
     * 币别编码
     */
    @Column(name = "CURRENCY_ID")
    private Integer currencyId;

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

    @Column(name = "TOTAL_SALES")
    private String totalSales;

    /**
     * 1:不含税，2:含税
     */
    @Column(name = "DEDUCTION_TAX")
    private String deductionTax;

    /**
     * @return CUSTOMER_ID
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return MYID
     */
    public String getMyid() {
        return myid;
    }

    /**
     * @param myid
     */
    public void setMyid(String myid) {
        this.myid = myid;
    }

    /**
     * 获取A:正常,I:删除
     *
     * @return STATUS - A:正常,I:删除
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置A:正常,I:删除
     *
     * @param status A:正常,I:删除
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return CREATED
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return LASTMOD
     */
    public Date getLastmod() {
        return lastmod;
    }

    /**
     * @param lastmod
     */
    public void setLastmod(Date lastmod) {
        this.lastmod = lastmod;
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
     * @return MODIFIYER - 修改人
     */
    public Integer getModifiyer() {
        return modifiyer;
    }

    /**
     * 设置修改人
     *
     * @param modifiyer 修改人
     */
    public void setModifiyer(Integer modifiyer) {
        this.modifiyer = modifiyer;
    }

    /**
     * 获取客户类型
     *
     * @return CLASS_ID - 客户类型
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * 设置客户类型
     *
     * @param classId 客户类型
     */
    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    /**
     * @return TEL
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return FAX
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return EMAIL
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取区域编码
     *
     * @return AREA_ID - 区域编码
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置区域编码
     *
     * @param areaId 区域编码
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取区域名称
     *
     * @return AREA_NAME - 区域名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置区域名称
     *
     * @param areaName 区域名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * 获取省份编码
     *
     * @return PROVINCE_ID - 省份编码
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * 设置省份编码
     *
     * @param provinceId 省份编码
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * 获取省份名称
     *
     * @return PROVINCE_NAME - 省份名称
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * 设置省份名称
     *
     * @param provinceName 省份名称
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * 获取城市编码
     *
     * @return CITY_ID - 城市编码
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * 设置城市编码
     *
     * @param cityId 城市编码
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取城市名称
     *
     * @return CITY_NAME - 城市名称
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * 设置城市名称
     *
     * @param cityName 城市名称
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return ADDRESS
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return ZIP
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * 获取客户等级编码
     *
     * @return LEVEL_ID - 客户等级编码
     */
    public Integer getLevelId() {
        return levelId;
    }

    /**
     * 设置客户等级编码
     *
     * @param levelId 客户等级编码
     */
    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    /**
     * 获取客户等级名称
     *
     * @return LEVEL_NAME - 客户等级名称
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * 设置客户等级名称
     *
     * @param levelName 客户等级名称
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * 获取价格等级
     *
     * @return PRICE_LEVEL - 价格等级
     */
    public Integer getPriceLevel() {
        return priceLevel;
    }

    /**
     * 设置价格等级
     *
     * @param priceLevel 价格等级
     */
    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    /**
     * 获取联系周期
     *
     * @return CONTACT_PERIOD - 联系周期
     */
    public Integer getContactPeriod() {
        return contactPeriod;
    }

    /**
     * 设置联系周期
     *
     * @param contactPeriod 联系周期
     */
    public void setContactPeriod(Integer contactPeriod) {
        this.contactPeriod = contactPeriod;
    }

    /**
     * 获取客户来源编码
     *
     * @return SOURCE_ID - 客户来源编码
     */
    public Integer getSourceId() {
        return sourceId;
    }

    /**
     * 设置客户来源编码
     *
     * @param sourceId 客户来源编码
     */
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 获取客户来源名称
     *
     * @return SOURCE_NAME - 客户来源名称
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * 设置客户来源名称
     *
     * @param sourceName 客户来源名称
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * 获取客户公司性质编码
     *
     * @return NATURE_ID - 客户公司性质编码
     */
    public Integer getNatureId() {
        return natureId;
    }

    /**
     * 设置客户公司性质编码
     *
     * @param natureId 客户公司性质编码
     */
    public void setNatureId(Integer natureId) {
        this.natureId = natureId;
    }

    /**
     * 获取客户公司性质名称
     *
     * @return NATURE_NAME - 客户公司性质名称
     */
    public String getNatureName() {
        return natureName;
    }

    /**
     * 设置客户公司性质名称
     *
     * @param natureName 客户公司性质名称
     */
    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    /**
     * 获取行业编码
     *
     * @return INDUSTRY_ID - 行业编码
     */
    public Integer getIndustryId() {
        return industryId;
    }

    /**
     * 设置行业编码
     *
     * @param industryId 行业编码
     */
    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    /**
     * 获取行业名称
     *
     * @return INDUSTRY_NAME - 行业名称
     */
    public String getIndustryName() {
        return industryName;
    }

    /**
     * 设置行业名称
     *
     * @param industryName 行业名称
     */
    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    /**
     * 获取主业务业
     *
     * @return MAIN_BUSINESS - 主业务业
     */
    public String getMainBusiness() {
        return mainBusiness;
    }

    /**
     * 设置主业务业
     *
     * @param mainBusiness 主业务业
     */
    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    /**
     * 获取公司规模编码
     *
     * @return SIZE_ID - 公司规模编码
     */
    public Integer getSizeId() {
        return sizeId;
    }

    /**
     * 设置公司规模编码
     *
     * @param sizeId 公司规模编码
     */
    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    /**
     * 获取公司规模
     *
     * @return SIZE_NAME - 公司规模
     */
    public String getSizeName() {
        return sizeName;
    }

    /**
     * 设置公司规模
     *
     * @param sizeName 公司规模
     */
    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    /**
     * 获取开业年份
     *
     * @return SETUP_DATE - 开业年份
     */
    public Date getSetupDate() {
        return setupDate;
    }

    /**
     * 设置开业年份
     *
     * @param setupDate 开业年份
     */
    public void setSetupDate(Date setupDate) {
        this.setupDate = setupDate;
    }

    /**
     * 获取注册资金
     *
     * @return CAPITAL - 注册资金
     */
    public Integer getCapital() {
        return capital;
    }

    /**
     * 设置注册资金
     *
     * @param capital 注册资金
     */
    public void setCapital(Integer capital) {
        this.capital = capital;
    }

    /**
     * 获取法人
     *
     * @return CORPORATION - 法人
     */
    public String getCorporation() {
        return corporation;
    }

    /**
     * 设置法人
     *
     * @param corporation 法人
     */
    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    /**
     * 获取信用编码
     *
     * @return CREDIT_ID - 信用编码
     */
    public Integer getCreditId() {
        return creditId;
    }

    /**
     * 设置信用编码
     *
     * @param creditId 信用编码
     */
    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    /**
     * 获取信用名称
     *
     * @return CREDIT_NAME - 信用名称
     */
    public String getCreditName() {
        return creditName;
    }

    /**
     * 设置信用名称
     *
     * @param creditName 信用名称
     */
    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    /**
     * @return BANK
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param bank
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * @return ACCOUNT
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取税号
     *
     * @return TAXNO - 税号
     */
    public String getTaxno() {
        return taxno;
    }

    /**
     * 设置税号
     *
     * @param taxno 税号
     */
    public void setTaxno(String taxno) {
        this.taxno = taxno;
    }

    /**
     * 获取共享人(以逗号分隔)
     *
     * @return SHARED - 共享人(以逗号分隔)
     */
    public String getShared() {
        return shared;
    }

    /**
     * 设置共享人(以逗号分隔)
     *
     * @param shared 共享人(以逗号分隔)
     */
    public void setShared(String shared) {
        this.shared = shared;
    }

    /**
     * 获取客户父项(不用)
     *
     * @return PID - 客户父项(不用)
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 设置客户父项(不用)
     *
     * @param pid 客户父项(不用)
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取附件编码
     *
     * @return ATTACHMENT_ID - 附件编码
     */
    public Integer getAttachmentId() {
        return attachmentId;
    }

    /**
     * 设置附件编码
     *
     * @param attachmentId 附件编码
     */
    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    /**
     * 获取描述
     *
     * @return DESCRIPTION - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取销售编码
     *
     * @return SALE_ID - 销售编码
     */
    public Integer getSaleId() {
        return saleId;
    }

    /**
     * 设置销售编码
     *
     * @param saleId 销售编码
     */
    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    /**
     * 获取销售名称
     *
     * @return SALE_NAME - 销售名称
     */
    public String getSaleName() {
        return saleName;
    }

    /**
     * 设置销售名称
     *
     * @param saleName 销售名称
     */
    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    /**
     * 获取销售组织编码
     *
     * @return SALE_ORGANIZATION_ID - 销售组织编码
     */
    public Integer getSaleOrganizationId() {
        return saleOrganizationId;
    }

    /**
     * 设置销售组织编码
     *
     * @param saleOrganizationId 销售组织编码
     */
    public void setSaleOrganizationId(Integer saleOrganizationId) {
        this.saleOrganizationId = saleOrganizationId;
    }

    /**
     * 获取销售组织名称
     *
     * @return SALE_ORGANIZATION_NAME - 销售组织名称
     */
    public String getSaleOrganizationName() {
        return saleOrganizationName;
    }

    /**
     * 设置销售组织名称
     *
     * @param saleOrganizationName 销售组织名称
     */
    public void setSaleOrganizationName(String saleOrganizationName) {
        this.saleOrganizationName = saleOrganizationName;
    }

    /**
     * 获取客户状态 T:交易中，S:禁用
     *
     * @return CUSTOMER_STATUS - 客户状态 T:交易中，S:禁用
     */
    public String getCustomerStatus() {
        return customerStatus;
    }

    /**
     * 设置客户状态 T:交易中，S:禁用
     *
     * @param customerStatus 客户状态 T:交易中，S:禁用
     */
    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * 获取客户类型名称
     *
     * @return CLASS_NAME - 客户类型名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置客户类型名称
     *
     * @param className 客户类型名称
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return EMP_COUNT
     */
    public Integer getEmpCount() {
        return empCount;
    }

    /**
     * @param empCount
     */
    public void setEmpCount(Integer empCount) {
        this.empCount = empCount;
    }

    /**
     * 获取税率
     *
     * @return TAX - 税率
     */
    public Integer getTax() {
        return tax;
    }

    /**
     * 设置税率
     *
     * @param tax 税率
     */
    public void setTax(Integer tax) {
        this.tax = tax;
    }

    /**
     * 获取立帐方式 1:出库立账,2:开票立帐,3:不立账(不用)
     *
     * @return SETUP_ACCOUNT - 立帐方式 1:出库立账,2:开票立帐,3:不立账(不用)
     */
    public Integer getSetupAccount() {
        return setupAccount;
    }

    /**
     * 设置立帐方式 1:出库立账,2:开票立帐,3:不立账(不用)
     *
     * @param setupAccount 立帐方式 1:出库立账,2:开票立帐,3:不立账(不用)
     */
    public void setSetupAccount(Integer setupAccount) {
        this.setupAccount = setupAccount;
    }

    /**
     * 获取币别编码
     *
     * @return CURRENCY_ID - 币别编码
     */
    public Integer getCurrencyId() {
        return currencyId;
    }

    /**
     * 设置币别编码
     *
     * @param currencyId 币别编码
     */
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return CURRENCY_NAME
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * @return TOTAL_SALES
     */
    public String getTotalSales() {
        return totalSales;
    }

    /**
     * @param totalSales
     */
    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * 获取1:不含税，2:含税
     *
     * @return DEDUCTION_TAX - 1:不含税，2:含税
     */
    public String getDeductionTax() {
        return deductionTax;
    }

    /**
     * 设置1:不含税，2:含税
     *
     * @param deductionTax 1:不含税，2:含税
     */
    public void setDeductionTax(String deductionTax) {
        this.deductionTax = deductionTax;
    }
}