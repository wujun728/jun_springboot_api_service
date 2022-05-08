package com.itboy.dao;

import com.itboy.model.SysUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName DbSqlTextRepository
 * @Description sql文本持久层
 * @Author 超
 * @Date 2019/6/22 0022 2:08
 **/
public interface SysUserLogRepository extends JpaSpecificationExecutor<SysUserLog>, JpaRepository<SysUserLog,Long> {


}
