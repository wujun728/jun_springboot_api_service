package com.jun.plugin.bizservice.mapper;

import com.jun.plugin.bizservice.entity.OaLearnInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 培训学习
 * 
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
public interface OaLearnInfoMapper extends BaseMapper<OaLearnInfoEntity> {

	@Select("SELECT count(1) from sys_user")
    int selectCountUser();
	
}
