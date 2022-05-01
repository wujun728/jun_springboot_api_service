package com.itboy.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName DbSourceModel
 * @Description 数据源BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 16:15
 **/
@Data
@Entity
@Table(name = "sql_dbcource")
public class DbSourceModel extends Pages {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String dbname;
    @Column
    private String dburl;
    @Transient
    private String dbport;
    @Column
    private String dbaccount;
    @Column
    private String dbpass;
    @Column
    private String dbstate ;
    @Column
    private int dbsync;
    @Column
    private String driverClass;
    @Column
    private int    initialSize;//=10;
    @Column
    private int    maxActive;//=20;
    @Column
    private int    maxIdle;//=5;
    @Column
    private int    maxWait;//=5000;
    @Column
    private String dbtestUrl;

    @Transient
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String value;
    @Transient
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String code;

}
