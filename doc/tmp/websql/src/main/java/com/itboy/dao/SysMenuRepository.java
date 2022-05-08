package com.itboy.dao;

import com.itboy.model.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMenuRepository extends JpaRepository<SysPermission,Long> {
}
