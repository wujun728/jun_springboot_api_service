package com.soukuan.enums;

import lombok.Getter;

/**
 * 枚举，定义token的三种状态
 */
 public enum TokenState {
     /**
      * 过期
      */
    EXPIRED("EXPIRED"),
    /**
     * 无效(token不合法)
     */
    INVALID("INVALID"), 
    /**
     * 有效的
     */
    VALID("VALID");  

    @Getter
    private String  state;
      
    TokenState(String state) {
        this.state = state;
    }

    /**
     * 根据状态字符串获取token状态枚举对象
     * @param tokenState
     * @return
     */
    public static TokenState getTokenState(String tokenState){
        for (TokenState state : TokenState.values()) {
            if(state.getState().equals(tokenState)){
               return state;
            }
        }
        return null;
    }

}  