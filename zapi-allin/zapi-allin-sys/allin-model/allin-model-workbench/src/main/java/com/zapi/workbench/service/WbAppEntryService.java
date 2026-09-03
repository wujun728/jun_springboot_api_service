package com.zapi.workbench.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workbench.dto.DragEntryDTO;
import com.zapi.workbench.entity.WbAppEntry;

import javax.validation.Valid;
import java.util.List;

/**
 * 应用条目服务接口
 */
public interface WbAppEntryService extends IService<WbAppEntry> {

    /**
     * 分页查询应用条目
     *
     * @param page 分页对象
     * @param appId 应用ID
     * @param name 条目名称（模糊查询）
     * @param type 条目类型
     * @param parentId 上级ID
     * @return 分页结果
     */
    Page<WbAppEntry> getAppEntryPage(Page<WbAppEntry> page, Long appId, String name, String type, Long parentId);

    /**
     * 根据应用ID获取树形结构的条目列表
     *
     * @param appId 应用ID
     * @return 树形结构列表
     */
    List<WbAppEntry> getAppEntryTree(Long appId);

    /**
     * 根据应用ID和父级ID获取子条目列表
     *
     * @param appId 应用ID
     * @param parentId 父级ID
     * @return 子条目列表
     */
    List<WbAppEntry> getAppEntryByParentId(Long appId, Long parentId);

    /**
     * 添加应用条目
     *
     * @param appEntry 应用条目对象
     * @return 是否成功
     */
    boolean addAppEntry(WbAppEntry appEntry);

    /**
     * 更新应用条目
     *
     * @param appEntry 应用条目对象
     * @return 是否成功
     */
    boolean updateAppEntry(WbAppEntry appEntry);

    /**
     * 删除应用条目（逻辑删除）
     *
     * @param id 条目ID
     * @return 是否成功
     */
    boolean deleteAppEntry(Long id);

    /**
     * 批量删除应用条目（逻辑删除）
     *
     * @param ids 条目ID列表
     * @return 是否成功
     */
    boolean batchDeleteAppEntry(List<Long> ids);

    /***
     * 拖拽排序
     * @param dragEntryDTO
     */
    void dragSort(@Valid DragEntryDTO dragEntryDTO);
}
