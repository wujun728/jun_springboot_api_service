package com.soukuan.behavior.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/8 0008. 
 * 具体容器接口 
 */  
public class ConcreteAggregate<T> implements Aggregate<T>{  
    private List<T> list = new ArrayList<>();
  
    @Override  
    public void add(T obj) {  
        list.add(obj);  
    }  
  
    @Override  
    public void remove(T obj) {  
        list.remove(obj);  
    }  
  
    @Override  
    public Iterator<T> iterator() {  
        return new ConcreteIterator<>();  
    }  
} 