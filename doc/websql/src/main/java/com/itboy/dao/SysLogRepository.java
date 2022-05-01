package com.itboy.dao;

import com.itboy.model.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRepository extends JpaSpecificationExecutor<SysLog>,JpaRepository<SysLog,Long> {
}
