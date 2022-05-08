package com.itboy.dao;

import com.itboy.model.DbSqlText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName DbSqlTextRepository
 * @Description sql文本持久层
 * @Author 超
 * @Date 2019/6/22 0022 2:08
 **/
public interface DbSqlTextRepository extends JpaSpecificationExecutor<DbSqlText>, JpaRepository<DbSqlText,Long> {

    @Transactional
    @Query("DELETE FROM DbSqlText WHERE id= ?1")
    @Modifying
    void delsqlText(Integer id);
}
