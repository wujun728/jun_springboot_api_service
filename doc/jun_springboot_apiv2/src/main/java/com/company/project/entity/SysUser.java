package com.company.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户
 *
 * @author wujun
 * @version V1.0
 * @date 2020年3月18日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity implements Serializable {
    @TableId
    private String id;

    @NotBlank(message = "账号不能为空")
    private String username;

    private String salt;

    @NotBlank(message = "密码不能为空")
    private String password;

    @TableField(exist = false)
    private String oldPwd;

    @TableField(exist = false)
    private String newPwd;

    private String phone;

    private String deptId;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String deptNo;


    private String realName;

    private String nickName;

    private String email;

    private Integer status;

    private Integer sex;

    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    private String createId;

    private String updateId;

    private Integer createWhere;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private List<String> roleIds;

    @TableField(exist = false)
    private String captcha;
    
    private String remark;
    
    private String leavel; 
    private Date borndate; 
    private String age; 
    private String dangyuan; 
    private String education1; 
    private String school1; 
    private String major1; 
    private String major2; 
    private Date majordate; 
    private String major_no; 
    private String school_type; 
    private String isfulltime; 
    private String school2; 
    private String edu_col1; 
    private Date edu_col2; 
    private String edu_col3; 
    private String edu_col4; 
    private String edu_col11; 
    private String edu_col12; 
    private Date edu_col13; 
    private String edu_col14; 
    private String edu_col15; 
    private String edu_col16; 
    private String edu_col17; 
    private Date edu_col18; 
    private String edu_col19; 
    private String eng_type; 
    private String other_lang; 
    private String other_exam; 
    private Date work_time_begin; 
    private String company_before_jonin; 
    private String company_before_jonin_job; 
    private String company_before_jonin2; 
    private String company_before_jonin_job2; 
    private Date join_company_time; 
    private String company_age; 
    private String job_title; 
    private String job_title_no; 
    private String isoutsitejob; 
    private String passcount; 
    private String noexamcount; 
    private String cpacount1; 
    private String cpapasscounte; 
    private Date cpadate1; 
    private String cpano; 
    private String cpacode; 
    private String cpacode2; 
    private String cpacode3; 
    private String cpacode4; 
    private String goodat; 
    private String homeadress; 
    private String telephone; 
    private String idno; 
}