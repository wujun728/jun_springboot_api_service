package com.soukuan.structure.proxy;

/**
 * 接口实现
 * 目标对象
 */
public class UserDaoImpl implements IUserDao {
    public void save() {
        System.out.println("----已经保存数据!----");
    }
}