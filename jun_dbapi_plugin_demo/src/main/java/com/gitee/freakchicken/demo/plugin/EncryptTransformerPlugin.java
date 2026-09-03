package com.gitee.freakchicken.demo.plugin;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * md5字段加密插件
 */
public class EncryptTransformerPlugin extends TransformPlugin {

    @Override
    public void init() {
        super.logger.info("EncryptTransformerPlugin init ...");
    }

    /**
     * MD5加密
     * @param data             执行器执行后返回的结果数据
     * @param localPluginParam 插件的局部参数
     * @return
     */
    @Override
    public Object transform(Object data, String localPluginParam) {
        List<JSONObject> list = (List<JSONObject>) data;
        if (StringUtils.isNoneBlank(localPluginParam)) {
            String[] columns = localPluginParam.split(";");
            list.stream().forEach(t -> {
                for (String column : columns) {
                    t.put(column, DigestUtils.md5Hex(t.getString(column)));
                }
            });
        }
        return list;
    }


    /**
     * 插件名称，用于在页面上显示，提示用户
     *
     * @return
     */
    @Override
    public String getName() {
        return "字段加密插件";
    }

    /**
     * 插件功能描述，用于在页面上显示，提示用户
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "对字段进行MD5加密";
    }

    /**
     * 插件参数描述，用于在页面上显示，提示用户
     *
     * @return
     */
    @Override
    public String getParamDescription() {
        return "插件参数填写要加密的字段，多个用英文分号;隔开，如果不填写则不用给任何字段加密";
    }
}
