package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.DepartmentDO;
import com.example.demo.entity.DepartmentLoadVO;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户管理
 * @Date 2020/4/24 7:43
 * @Created by 王弘博
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/load")
    public DepartmentLoadVO load() {

        DepartmentDO topDepartment = departmentService.getById(100);

        DepartmentLoadVO vo = new DepartmentLoadVO();

        BeanUtils.copyProperties(topDepartment, vo);

        vo.setChirdDepartmentList(getChildDepartment(100));

        return vo;

    }

    private List<DepartmentLoadVO> getChildDepartment(Integer parentId) {

        List<DepartmentLoadVO> resultList = new ArrayList<>();

        List<DepartmentDO> departmentDOS = departmentService.list(new QueryWrapper<DepartmentDO>().eq("parent_id", parentId));

        for (DepartmentDO departmentDO : departmentDOS) {

            DepartmentLoadVO vo = new DepartmentLoadVO();

            vo.setId(departmentDO.getId());
            vo.setParentId(departmentDO.getParentId());
            vo.setChirdDepartmentList(getChildDepartment(departmentDO.getId()));

            resultList.add(vo);
        }

        return resultList;
    }
}
