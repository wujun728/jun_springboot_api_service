package com.ruoyi.api.plugin.impl;

import com.ruoyi.api.plugin.TransformPlugin;

public class EncryptPlugin extends TransformPlugin {

    @Override
    public void init() {
        this.logger.info("EncryptPlugin initialized");
    }

    @Override
    public Object transform(Object data, String localPluginParam) {
        if (data == null) return null;
        String str = data.toString();
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append((char) (c + 1));
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return "Simple Encrypt";
    }

    @Override
    public String getDescription() {
        return "Simple character shift encryption transform";
    }

    @Override
    public String getParamDescription() {
        return "No parameters required";
    }
}