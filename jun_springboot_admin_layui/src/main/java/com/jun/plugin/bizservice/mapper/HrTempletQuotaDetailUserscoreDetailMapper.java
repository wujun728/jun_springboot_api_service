package com.jun.plugin.bizservice.mapper;

import com.jun.plugin.bizservice.entity.HrTempletQuotaDetailUserscoreDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户考核明细
 * 
 * @author wujun
 * @email wujun728@mail.com
 * @date 2021-10-20 16:28:52
 */
public interface HrTempletQuotaDetailUserscoreDetailMapper extends BaseMapper<HrTempletQuotaDetailUserscoreDetailEntity> {

	@Select("SELECT count(1) from sys_user")
    int selectCountUser();
	
}
