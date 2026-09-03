package com.zapi.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.form.model.entity.Form;
import com.zapi.form.service.FormService;
import com.zapi.grid.model.entity.Grid;
import com.zapi.grid.service.GridService;
import com.zapi.workbench.dto.DragEntryDTO;
import com.zapi.workbench.dto.QueryEntryDTO;
import com.zapi.workbench.entity.WbAppEntry;
import com.zapi.workbench.mapper.WbAppEntryMapper;
import com.zapi.workbench.service.WbAppEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 应用条目服务实现类
 */
@Service
public class WbAppEntryServiceImpl extends ServiceImpl<WbAppEntryMapper, WbAppEntry> implements WbAppEntryService {

    @Autowired
    private FormService formService;

    @Autowired
    private GridService gridService;

    @Override
    public Page<WbAppEntry> getAppEntryPage(Page<WbAppEntry> page, Long appId, String name, String type, Long parentId) {
        LambdaQueryWrapper<WbAppEntry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(appId != null, WbAppEntry::getAppId, appId)
                .like(StringUtils.hasText(name), WbAppEntry::getName, name)
                .eq(StringUtils.hasText(type), WbAppEntry::getType, type)
                .eq(parentId != null, WbAppEntry::getParentId, parentId)
                .eq(WbAppEntry::getIsDeleted, 0)
                .orderByAsc(WbAppEntry::getSort)
                .orderByDesc(WbAppEntry::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<WbAppEntry> getAppEntryTree(Long appId) {
        LambdaQueryWrapper<WbAppEntry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(appId != null, WbAppEntry::getAppId, appId)
                .eq(WbAppEntry::getIsDeleted, 0)
                .orderByAsc(WbAppEntry::getSort)
                .orderByDesc(WbAppEntry::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public List<WbAppEntry> getAppEntryByParentId(Long appId, Long parentId) {
        LambdaQueryWrapper<WbAppEntry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(appId != null, WbAppEntry::getAppId, appId)
                .eq(WbAppEntry::getParentId, parentId)
                .eq(WbAppEntry::getIsDeleted, 0)
                .orderByAsc(WbAppEntry::getSort)
                .orderByDesc(WbAppEntry::getCreateTime);
        return this.list(wrapper);
    }

    /**
     * 添加应用条目
     * 如果类型为表单(form)，则自动创建对应的表单数据
     * 如果类型为表格(grid)，则自动创建对应的表格数据
     * 
     * @param appEntry 应用条目
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAppEntry(WbAppEntry appEntry) {
        // 设置默认值
        if (appEntry.getIsDeleted() == null) {
            appEntry.setIsDeleted(0);
        }
        if (appEntry.getHasFlow() == null) {
            appEntry.setHasFlow(0);
        }
        
        // 保存应用条目
        boolean saved = this.save(appEntry);
        
        // 如果是表单类型，自动创建对应的表单数据
        if (saved && "form".equals(appEntry.getType())) {
            Form form = new Form();
            form.setFormName(appEntry.getName());
            form.setFormDescription("自动创建的表单：" + appEntry.getName());
            form.setEntryId(appEntry.getId());
            form.setStatus(1); // 默认启用
            form.setFormConfig("{}"); // 默认空配置
            form.setDelFlag(0); // 未删除
            
            // 创建表单
            String formId = formService.createForm(form);
            if (formId == null) {
                throw new RuntimeException("创建表单失败");
            }
        }
        
        // 如果是表格类型，自动创建对应的表格数据
        if (saved && "grid".equals(appEntry.getType())) {
            Grid grid = new Grid();
            grid.setGridName(appEntry.getName());
            grid.setGridDescription("自动创建的表格：" + appEntry.getName());
            grid.setEntryId(appEntry.getId());
            grid.setStatus(1); // 默认启用
            grid.setGridConfig("{}"); // 默认空配置
            grid.setDelFlag(0); // 未删除
            
            // 创建表格
            boolean gridSaved = gridService.save(grid);
            if (!gridSaved) {
                throw new RuntimeException("创建表格失败");
            }
        }
        
        return saved;
    }

    @Override
    public boolean updateAppEntry(WbAppEntry appEntry) {
        return this.updateById(appEntry);
    }

    /**
     * 删除应用条目（逻辑删除）
     * 递归删除子节点，如果是表单类型则同时删除关联的表单数据，如果是表格类型则删除关联的表格数据
     * 
     * @param id 条目ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppEntry(Long id) {
        // 获取条目信息
        WbAppEntry appEntry = this.getById(id);
        if (appEntry == null) {
            return false;
        }
        
        // 先递归删除所有子节点
        List<WbAppEntry> children = this.getAppEntryByParentId(appEntry.getAppId(), id);
        if (children != null && !children.isEmpty()) {
            for (WbAppEntry child : children) {
                deleteAppEntry(child.getId()); // 递归删除
            }
        }
        
        // 如果是表单类型，删除关联的表单数据
        if ("form".equals(appEntry.getType())) {
            try {
                // 查询关联的表单
                LambdaQueryWrapper<Form> formWrapper = new LambdaQueryWrapper<>();
                formWrapper.eq(Form::getEntryId, id)
                           .eq(Form::getDelFlag, 0);
                List<Form> forms = formService.list(formWrapper);
                
                // 逻辑删除所有关联的表单（使用 MyBatis-Plus 的逻辑删除）
                if (forms != null && !forms.isEmpty()) {
                    for (Form form : forms) {
                        formService.removeById(form.getId());
                    }
                }
            } catch (Exception e) {
                // 如果表单删除失败，记录日志但不影响条目删除
                System.err.println("删除关联表单失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 如果是表格类型，删除关联的表格数据
        if ("grid".equals(appEntry.getType())) {
            try {
                // 查询关联的表格
                LambdaQueryWrapper<Grid> gridWrapper = new LambdaQueryWrapper<>();
                gridWrapper.eq(Grid::getEntryId, id)
                           .eq(Grid::getDelFlag, 0);
                List<Grid> grids = gridService.list(gridWrapper);
                
                // 逻辑删除所有关联的表格（使用 MyBatis-Plus 的逻辑删除）
                if (grids != null && !grids.isEmpty()) {
                    for (Grid grid : grids) {
                        gridService.removeById(grid.getId());
                    }
                }
            } catch (Exception e) {
                // 如果表格删除失败，记录日志但不影响条目删除
                System.err.println("删除关联表格失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 使用 MyBatis-Plus 的逻辑删除方法
        return this.removeById(id);
    }

    @Override
    public boolean batchDeleteAppEntry(List<Long> ids) {
        // 批量逻辑删除
        return ids.stream().allMatch(this::deleteAppEntry);
    }

    /**
     * 拖拽排序实现
     * 
     * 排序算法说明：
     * 1. 使用整数排序值，默认间隔为1024，确保有足够的插入空间
     * 2. 拖拽到首位：sort = firstSort / 2（如果空间不足则重排所有节点）
     * 3. 拖拽到中间：sort = (beforeSort + afterSort) / 2（如果空间不足则调整后续节点）
     * 4. 拖拽到末尾：sort = lastSort + 1024
     * 5. 支持跨父级拖拽，自动更新parentId（包括拖拽到顶级，此时parentId为null）
     * 
     * @param dragEntryDTO 拖拽排序参数
     */
    @Override
    public void dragSort(DragEntryDTO dragEntryDTO) {
        // ========== 第一步：验证并获取被拖拽的条目 ==========
        WbAppEntry dragEntry = this.getById(dragEntryDTO.getEntryId());
        if (dragEntry == null) {
            throw new RuntimeException("被拖拽的条目不存在");
        }

        // ========== 第二步：处理父级变更（跨分组拖拽或拖拽到顶级） ==========
        // 始终更新父级ID，支持拖拽到顶级（parentId为null）
        // 注意：这里不能用 != null 判断，因为拖拽到顶级时 parentId 就是 null
        dragEntry.setParentId(dragEntryDTO.getParentId());

        // ========== 第三步：获取目标位置的同级所有条目 ==========
        // 获取拖拽后所在层级的所有兄弟节点，用于计算新的排序值
        List<WbAppEntry> siblings = this.getAppEntryByParentId(
            dragEntry.getAppId(), 
            dragEntry.getParentId()
        );

        // ========== 第四步：根据拖拽位置计算新的排序值 ==========
        Integer newSort;
        Long beforeEntryId = dragEntryDTO.getBeforeEntryId();
        
        if (beforeEntryId == null || beforeEntryId == 0) {
            // ---------- 场景1：拖拽到首位 ----------
            if (siblings.isEmpty()) {
                // 如果目标位置没有任何节点，使用默认排序值
                newSort = 1024;
            } else {
                // 获取当前首位节点的排序值
                Integer firstSort = siblings.get(0).getSort();
                if (firstSort == null) {
                    firstSort = 1024;
                }
                
                if (firstSort > 4) {
                    // 如果首位节点的排序值足够大（>4），直接取一半作为新排序值
                    // 例如：firstSort=1024，则newSort=512
                    newSort = firstSort / 2;
                } else {
                    // 如果首位节点的排序值太小，空间不足，需要重新分配所有节点的排序值
                    // 重排策略：从第二个位置开始，每个节点间隔1024
                    // 例如：原有节点变为 2048, 3072, 4096...，新节点插入到1024
                    for (int i = 0; i < siblings.size(); i++) {
                        WbAppEntry sibling = siblings.get(i);
                        // 跳过被拖拽的节点本身（避免重复更新）
                        if (!sibling.getId().equals(dragEntry.getId())) {
                            sibling.setSort((i + 2) * 1024);
                            this.updateById(sibling);
                        }
                    }
                    newSort = 1024;
                }
            }
        } else {
            // ---------- 场景2：拖拽到指定位置之后 ----------
            WbAppEntry beforeEntry = null;  // 前驱节点（拖拽位置的前一个节点）
            WbAppEntry afterEntry = null;   // 后驱节点（拖拽位置的后一个节点）
            
            // 遍历同级节点，找到前驱和后驱节点
            for (int i = 0; i < siblings.size(); i++) {
                if (siblings.get(i).getId().equals(beforeEntryId)) {
                    beforeEntry = siblings.get(i);
                    
                    // 从当前位置的下一个开始查找后驱节点
                    if (i + 1 < siblings.size()) {
                        // 找到下一个不是被拖拽节点的条目作为后驱
                        // （因为被拖拽节点可能原本就在这个位置附近）
                        for (int j = i + 1; j < siblings.size(); j++) {
                            if (!siblings.get(j).getId().equals(dragEntry.getId())) {
                                afterEntry = siblings.get(j);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            
            // 验证前驱节点是否存在
            if (beforeEntry == null) {
                throw new RuntimeException("前驱节点不存在");
            }
            
            // 获取前驱节点的排序值，如果为空则使用默认值1024
            Integer beforeSort = beforeEntry.getSort() != null ? beforeEntry.getSort() : 1024;
            
            if (afterEntry == null) {
                // ---------- 场景2.1：拖拽到末尾（前驱节点后面没有其他节点） ----------
                // 直接在前驱节点的排序值基础上加1024
                // 例如：beforeSort=3072，则newSort=4096
                newSort = beforeSort + 1024;
            } else {
                // ---------- 场景2.2：拖拽到中间位置（前驱和后驱节点之间） ----------
                Integer afterSort = afterEntry.getSort() != null ? afterEntry.getSort() : beforeSort + 2048;
                
                if (afterSort - beforeSort > 4) {
                    // 如果前后节点之间的间隔足够大（>4），直接取中间值
                    // 例如：beforeSort=1024, afterSort=3072，则newSort=2048
                    newSort = (beforeSort + afterSort) / 2;
                } else {
                    // 如果前后节点之间的间隔太小（<=4），空间不足，需要调整后续节点
                    // 调整策略：将后驱节点及其后面的所有节点的排序值都增加1024
                    int adjustIndex = siblings.indexOf(afterEntry);
                    for (int i = adjustIndex; i < siblings.size(); i++) {
                        WbAppEntry sibling = siblings.get(i);
                        // 跳过被拖拽的节点本身
                        if (!sibling.getId().equals(dragEntry.getId())) {
                            sibling.setSort(sibling.getSort() + 1024);
                            this.updateById(sibling);
                        }
                    }
                    // 调整后重新计算中间值
                    newSort = (beforeSort + afterSort + 1024) / 2;
                }
            }
        }
        
        // ========== 第五步：更新被拖拽条目的排序值和父级ID ==========
        dragEntry.setSort(newSort);
        this.updateById(dragEntry);
    }
}
