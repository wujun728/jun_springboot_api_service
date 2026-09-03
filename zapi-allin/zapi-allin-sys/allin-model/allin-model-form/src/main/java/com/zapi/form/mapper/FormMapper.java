package com.zapi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zapi.form.model.entity.Form;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表单 Mapper 接口
 *
 * @author zapi
 * @since 2024-09-24
 */
@Mapper
public interface FormMapper extends BaseMapper<Form> {

}
