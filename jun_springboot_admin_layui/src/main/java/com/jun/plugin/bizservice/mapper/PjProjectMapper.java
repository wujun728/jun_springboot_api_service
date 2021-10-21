package com.jun.plugin.bizservice.mapper;

import com.jun.plugin.bizservice.entity.PjProjectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 项目信息
 * 
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:53
 */
public interface PjProjectMapper extends BaseMapper<PjProjectEntity> {

	@Select("SELECT count(1) from sys_user")
    int selectCountUser();
	
}
