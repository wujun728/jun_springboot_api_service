package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.DepartmentDAO;
import com.example.demo.entity.DepartmentDO;
import com.example.demo.service.DepartmentService;
import org.springframework.stereotype.Service;

/**
 * @Description DepartmentServiceImpl
 * @Date 2020/4/24 7:41
 * @Created by 王弘博
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentDAO, DepartmentDO> implements DepartmentService {
}
