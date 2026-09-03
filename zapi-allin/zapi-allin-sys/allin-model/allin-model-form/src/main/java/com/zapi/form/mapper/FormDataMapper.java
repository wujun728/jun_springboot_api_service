package com.zapi.form.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 动态业务数据 Mapper
 */
@Mapper
public interface FormDataMapper {

    int insertDynamic(@Param("tableName") String tableName,
                      @Param("entries") List<Map<String, Object>> entries);

    int updateByIdDynamic(@Param("tableName") String tableName,
                          @Param("id") Long id,
                          @Param("entries") List<Map<String, Object>> entries);

    // 旧的 AND-only 方法已移除，统一使用 DNF 版本

    /**
     * 按 DNF 条件更新：criteriaGroups 表示若干 AND 条件组，组与组之间为 OR
     * 例如：[(a=1,b=2),(c=3)] => WHERE is_deleted=0 AND ((a=1 AND b=2) OR (c=3))
     */
    int updateByCriteriaGroupsDynamic(@Param("tableName") String tableName,
                                      @Param("criteriaGroups") List<List<Map<String, Object>>> criteriaGroups,
                                      @Param("entries") List<Map<String, Object>> entries);

    /**
     * 按 DNF 条件删除：criteriaGroups 表示若干 AND 条件组，组与组之间为 OR
     * 例如：[(a=1,b=2),(c=3)] => WHERE is_deleted=0 AND ((a=1 AND b=2) OR (c=3))
     */
    int deleteByCriteriaGroupsDynamic(@Param("tableName") String tableName,
                                     @Param("criteriaGroups") List<List<Map<String, Object>>> criteriaGroups);

    Map<String, Object> selectByIdDynamic(@Param("tableName") String tableName,
                                          @Param("id") Long id);

    int deleteByIdDynamic(@Param("tableName") String tableName,
                          @Param("id") Long id);

    List<Map<String, Object>> pageQueryDynamic(@Param("tableName") String tableName,
                                               @Param("criteria") List<Map<String, Object>> criteria,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    Long countQueryDynamic(@Param("tableName") String tableName,
                           @Param("criteria") List<Map<String, Object>> criteria);

    int batchDeleteByIds(@Param("tableName") String tableName,
                         @Param("ids") List<Long> ids);
} 