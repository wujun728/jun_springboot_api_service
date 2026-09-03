package com.zapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门Mapper接口
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
} 