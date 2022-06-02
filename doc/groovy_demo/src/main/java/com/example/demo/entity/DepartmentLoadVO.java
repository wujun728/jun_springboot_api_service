package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description TODO
 * @Date 2020/5/21 11:06
 * @Created by 王弘博
 */
@Data
public class DepartmentLoadVO implements Serializable {

    private Integer id;

    private Integer parentId;

    private String ancestors;

    private String departmentName;

    private Integer orderNum;

    private String status;

    private String createTime;

    private List<DepartmentLoadVO> chirdDepartmentList;
}
