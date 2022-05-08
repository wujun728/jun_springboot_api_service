package com.itboy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName SysLog
 * @Description 操作日志
 * @Author 超 boy_0214@sina.com
 * @Date 2019/7/4 0004 11:49
 **/
@Entity
@Table(name = "SYS_LOG")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SysLog extends Pages{

    @Id
    @GenericGenerator(name="generator",strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer id;

    @Column
    private String logName;

    @Column
    private String logType;

    @Column
    private String logDbSource;

    @Column(columnDefinition="TEXT")
    private String logContent;

    @Column(columnDefinition="TEXT")
    private String logResult;

    @Column
    private String logIp;

    @Column
    private String userid;

    @Column
    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private String logDate;

}
