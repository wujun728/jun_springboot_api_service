package com.itboy.dao;

import com.itboy.model.TimingVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TimingRepository extends JpaSpecificationExecutor<TimingVo>,JpaRepository<TimingVo,Long> {



}
