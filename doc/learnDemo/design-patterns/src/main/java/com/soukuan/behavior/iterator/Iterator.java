package com.soukuan.behavior.iterator;

/**
 * Created by Administrator on 2016/5/8 0008.  
 * 迭代器接口  
 */  
public interface Iterator<T> {  
    /** 
     * 是否还有下一个元素 
     * @return  true 表示有，false 表示没有 
     * */  
    boolean hasNext();  
  
    /** 
     * 返回当前位置的元素并将位置移至下一位 
     * */  
     T next();

}