package com.itboy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itboy.service.DbSourceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName
 * @Description 系统配置
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/22 0022 1:52
 **/
@Data
@Entity
@Table(name = "SYS_SETUP")
public class SysSetup {

    private SysSetup(){
    }
    private static class SysSetupHolder{
        private final static SysSetup instance = new SysSetup();
    }
    public static SysSetup getInstance(){
        return SysSetupHolder.instance;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private int initDbsource;//主动初始化数据源

    @Column
    private String col1;

    @Column
    private String col2;

    @Column
    private int col3;//开启sql执行记录功能

    @Column
    private int col4;


}
