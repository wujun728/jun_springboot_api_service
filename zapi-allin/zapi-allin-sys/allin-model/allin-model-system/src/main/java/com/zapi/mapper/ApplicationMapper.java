package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.Application;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
}