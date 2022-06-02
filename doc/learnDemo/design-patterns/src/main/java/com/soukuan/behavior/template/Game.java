package com.soukuan.behavior.template;

public abstract class Game {

   final void initialize(){
      System.out.println("初始化游戏环境！");
   }

   abstract void startPlay();

   final void endPlay(){
      System.out.println("关闭游戏！");
   }

   //模板
   public final void play(){

      //初始化游戏
      initialize();

      //开始游戏
      startPlay();

      //结束游戏
      endPlay();
   }
}