package com.itboy.model;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @ClassName Result
 * @Description 分页BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 17:58
 **/
@Data
public class Result<T> {

    int page;//起始页
    int limit;//页数大小
    int count;//数据数量
    String code;//代码
    String msg;//信息
    List<T> data;//返回数据
    T example;//任何类型条件

}
