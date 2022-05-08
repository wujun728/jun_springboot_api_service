package com.itboy.dao;

import com.itboy.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUserName(String userName);


}
