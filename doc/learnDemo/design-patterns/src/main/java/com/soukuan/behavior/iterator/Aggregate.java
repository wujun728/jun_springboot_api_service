package com.soukuan.behavior.iterator;

/**
 * Created by Administrator on 2016/5/8 0008. 
 * 容器接口 
 */  
public interface Aggregate<T> {  
    /** 
     * 添加一个元素 
     * @param obj 元素对象 
     * */  
    void add(T obj);  
  
    /** 
     * 移除一个元素 
     * @param obj 元素对象 
     * */  
    void remove(T obj);  
  
    /** 
     * 获取容器的迭代器 
     * @return 迭代器对象 
     * */  
    Iterator<T>  iterator();  
  
}  