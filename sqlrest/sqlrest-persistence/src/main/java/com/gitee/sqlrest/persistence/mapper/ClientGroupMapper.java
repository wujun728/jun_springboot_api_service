package com.gitee.sqlrest.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.sqlrest.common.dto.IdWithName;
import com.gitee.sqlrest.persistence.entity.ClientGroupEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ClientGroupMapper extends BaseMapper<ClientGroupEntity> {

  @Select("SELECT g.id,g.name from `SQLREST_CLIENT_GROUP` a LEFT JOIN `SQLREST_API_GROUP` g on a.group_id=g.id where a.client_id= #{id}")
  List<IdWithName> getGroupAuth(@Param("id") Long id);
}
