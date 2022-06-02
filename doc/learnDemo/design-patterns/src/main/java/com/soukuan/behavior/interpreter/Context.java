package com.soukuan.behavior.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<Variable,Integer> map = new HashMap<>();
    
    public void assign(Variable var , int value){
        map.put(var, new Integer(value));
    }
    
    public int lookup(Variable var) throws IllegalArgumentException{
        Integer value = map.get(var);
        if(value == null){
            throw new IllegalArgumentException();
        }
        return value.intValue();
    }
}