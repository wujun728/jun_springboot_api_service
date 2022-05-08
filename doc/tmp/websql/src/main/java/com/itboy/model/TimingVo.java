package com.itboy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName DbSourceModel
 * @Description 数据源BEAN
 * @Author  超 boy_0214@sina.com
 * @Date 2019/6/14 0014 16:15
 **/
@Data
@Entity
@Table(name = "sql_timing")
@EntityListeners(AuditingEntityListener.class)
public class TimingVo extends Pages {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String timingName;//作业数据源
    @Column
    private String syncName;//同步数据源
    @Column
    private String syncTable;//同步表名
    @Column
    private String executeTime;//执行时间
    @Column(columnDefinition="TEXT")
    private String sqlText;//执行的sql
    @Column
    private String col1;//状态
    @Column
    private String col2;//任务标题
    @Column
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private String sqlCreateDate;
    @Column
    private String sqlCreateUser;

}
