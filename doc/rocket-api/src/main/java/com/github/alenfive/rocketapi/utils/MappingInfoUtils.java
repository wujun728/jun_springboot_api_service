package com.github.alenfive.rocketapi.utils;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Set;

public class MappingInfoUtils {
    public static Set<String> getPatterns(RequestMappingInfo info){
        return info.getPatternsCondition() == null?info.getPathPatternsCondition().getPatternValues():info.getPatternsCondition().getPatterns();
    }
}
