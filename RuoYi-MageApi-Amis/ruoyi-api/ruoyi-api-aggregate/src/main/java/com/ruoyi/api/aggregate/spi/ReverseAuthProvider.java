package com.ruoyi.api.aggregate.spi;

import java.util.Map;

public interface ReverseAuthProvider {

    boolean authenticate(Map<String, String> authHeaders);

    String getType();
}