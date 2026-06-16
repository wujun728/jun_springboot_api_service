package com.ruoyi.business.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.mapper.TestDataMapper;
import com.ruoyi.business.domain.TestData;
import com.ruoyi.business.service.ITestDataService;

/**
 * 测试数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-15
 */
@Service
public class TestDataServiceImpl extends ServiceImpl<TestDataMapper, TestData> implements ITestDataService {
}
