package com.zapi.dataset.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVO<T> {
    private List<T> data;         // 当前页数据
    private int totalRecords;     // 总记录数
    private int pageNum;          // 当前页码
    private int pageSize;         // 每页大小
    private int totalPages;       // 总页数
}
