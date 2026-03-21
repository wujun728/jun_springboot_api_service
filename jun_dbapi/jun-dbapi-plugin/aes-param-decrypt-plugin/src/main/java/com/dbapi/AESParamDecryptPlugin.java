package com.dbapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginConf;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class AESParamDecryptPlugin extends ParamProcessPlugin {

    @Override
    public void init() {
        this.logger.info(getName() + "初始化");
    }

    @Override
    public String getName() {
        return "AES参数解密插件";
    }

    @Override
    public String getDescription() {
        return "对参数值使用AES算法解密";
    }

    @Override
    public String getParamDescription() {
        return "参数格式：{\"fields\":[\"aa\",\"bb\"],\"secret\":\"xxx\"}，其中fields表示要解密的参数名称，secret表示密钥，必须是16位字符串。fields可不填，如果不填，则对所有参数进行解密；secret可不填，如果不填就从全局插件参数中获取secret";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(Map<String, Object> map, ApiConfig apiConfig, String param) {
        JSONObject p = new JSONObject();
        if (param != null && !param.isEmpty()) {
            p = JSON.parseObject(param);
        }

        String secret = p.getString("secret");
        if (secret == null || secret.isEmpty()) {
            secret = PluginConf.getKey("AESParamDecryptPlugin.secret");
        }
        if (secret == null || secret.isEmpty()) {
            this.logger.error("插件全局参数和局部参数中都缺少加密密钥secret");
            throw new RuntimeException("插件全局参数和局部参数中都缺少加密密钥secret");
        }
        if (secret.length() != 16) {
            this.logger.error("加密密钥secret长度必须16位");
            throw new RuntimeException("加密密钥secret长度必须16位");
        }

        JSONArray fields = p.getJSONArray("fields");
        List<String> paramToEncryptList = fields == null ? new ArrayList<>() : fields.toJavaList(String.class);

        for (String key : map.keySet()) {
            if ((fields != null && paramToEncryptList.contains(key)) || fields == null) {
                try {
                    map.put(key, decrypt(map.get(key).toString(), secret));
                } catch (Exception e) {
                    this.logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String decrypt(String encryptedData, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] originalBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decrypted = new String(originalBytes);
        this.logger.info("解密成功: {}", decrypted);
        return decrypted;
    }
}
