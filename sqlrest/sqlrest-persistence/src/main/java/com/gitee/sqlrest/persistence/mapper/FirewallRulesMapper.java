package com.gitee.sqlrest.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.sqlrest.common.enums.OnOffEnum;
import com.gitee.sqlrest.persistence.entity.FirewallRulesEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface FirewallRulesMapper extends BaseMapper<FirewallRulesEntity> {

  @Update("update `SQLREST_FIREWALL_RULES` set status = #{status}　where id = #{id}")
  void updateStatus(@Param("id") Long id, @Param("status") OnOffEnum status);

}
