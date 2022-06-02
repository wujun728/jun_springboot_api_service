package com.soukuan.behavior.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/8 0008. 
 * 具体迭代器 
 */  
public class ConcreteIterator<T> implements Iterator{  
    private List<T> list = new ArrayList<>();
    private int cursor = 0;  
  
    @Override  
    public boolean hasNext() {  
        return cursor != list.size();  
    }  
  
    @Override  
    public Object next() {  
        T obj = null;  
        if(this.hasNext()){  
            obj = this.list.get(cursor++);  
        }  
        return obj;  
    }  
}