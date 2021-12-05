package com.company.project.common.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "openapi")
@Data
public class OpenapiConfig {
 
    private List<App> apps;
}

