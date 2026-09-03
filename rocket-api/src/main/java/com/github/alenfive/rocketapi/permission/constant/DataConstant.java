package com.github.alenfive.rocketapi.permission.constant;

/**
 * @author bxf
 * @version 1.0
 * @date 2023/11/08 11:39
 */
public interface DataConstant {


    interface ListDataType {
        /**
         * 数据集
         */
        String datasource = "1";
        /**
         * 列表
         */
        String apilist = "2";
        /**
         * 目录数据集
         */
        String dirlist = "3";

    }
    /**
     * api接口
     */
    interface ApiDataList {

        /**
         * api的查询接口权限
         */
        String VIEW = "rocketapi:api:view";

        /**
         * api的添加接口权限
         */
        String ADD = "rocketapi:api:add";

        /**
         * api的编辑接口权限
         */
        String UPDATE = "rocketapi:api:update";


        /**
         * api的删除接口权限
         */
        String DELETE = "rocketapi:api:delete";

    }
    /**
     * 数据源接口
     */
    interface DataSourceList {

        /**
         * 数据源的查询接口权限
         */
        String VIEW = "rocketapi:datasource:view";

        /**
         * 数据源的添加接口权限
         */
        String ADD = "rocketapi:datasource:add";

        /**
         * 数据源的编辑接口权限
         */
        String UPDATE = "rocketapi:datasource:update";


        /**
         * 数据源的删除接口权限
         */
        String DELETE = "rocketapi:datasource:delete";

    }

    /**
     * 目录接口
     */
    interface DirDataList {

        /**
         * 目录的查询接口权限
         */
        String VIEW = "rocketapi:dir:view";

        /**
         * 目录的添加接口权限
         */
        String ADD = "rocketapi:dir:add";

        /**
         * 目录的编辑接口权限
         */
        String UPDATE = "rocketapi:dir:update";


        /**
         * 目录的删除接口权限
         */
        String DELETE = "rocketapi:dir:delete";

    }

}
