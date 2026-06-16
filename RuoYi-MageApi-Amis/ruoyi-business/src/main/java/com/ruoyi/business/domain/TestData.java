package com.ruoyi.business.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 测试数据对象 test_data
 *
 * @author ruoyi
 * @date 2024-06-15
 */

@Data
@TableName("test_data")
public class TestData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;
    /** 名称 */
    @Excel(name = "名称")
    private String name;
}
