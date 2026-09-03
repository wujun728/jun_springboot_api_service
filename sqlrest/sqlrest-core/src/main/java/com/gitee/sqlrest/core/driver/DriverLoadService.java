package com.gitee.sqlrest.core.driver;

import cn.hutool.core.io.FileUtil;
import com.gitee.sqlrest.common.enums.ProductTypeEnum;
import com.gitee.sqlrest.core.dto.DatabaseTypeDriverResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DriverLoadService {

  private Map<ProductTypeEnum, Map<String, File>> drivers = new EnumMap<>(ProductTypeEnum.class);

  @Value("${datasource.driver.base-path}")
  private String driversBasePath;

  @EventListener(ApplicationReadyEvent.class)
  public void loadDrivers() {
    try {
      doLoadDrivers();
      log.info("Finish load jdbc drivers from local path: {}", driversBasePath);
    } catch (Exception e) {
      log.error("load drivers failed:{}", e.getMessage(), e);
      throw e;
    }
  }

  private void doLoadDrivers() {
    File file = new File(driversBasePath);
    File[] types = file.listFiles();
    if (ArrayUtils.isEmpty(types)) {
      throw new IllegalArgumentException(
          "No drivers type found from path:" + driversBasePath);
    }
    for (File type : types) {
      if (!ProductTypeEnum.exists(type.getName())) {
        continue;
      }
      File[] driverVersions = type.listFiles();
      if (ArrayUtils.isEmpty(driverVersions)) {
        throw new IllegalArgumentException(
            "No driver version found from path:" + type.getAbsolutePath());
      }
      for (File driverVersion : driverVersions) {
        if (ArrayUtils.isEmpty(driverVersion.listFiles())) {
          throw new IllegalArgumentException(
              "No driver version jar file found from path:" + driverVersion.getAbsolutePath());
        }
        ProductTypeEnum typeEnum = ProductTypeEnum.of(type.getName());
        Map<String, File> versionMap = drivers.computeIfAbsent(typeEnum, k -> new HashMap<>());
        versionMap.put(driverVersion.getName(), driverVersion);
        log.info("Load driver for {} ,version:{},path:{}",
            typeEnum.getName(), driverVersion.getName(), driverVersion.getAbsolutePath());
      }
    }
  }

  public List<String> getDriverVersion(ProductTypeEnum dbTypeEnum) {
    return Optional.ofNullable(drivers.get(dbTypeEnum)).orElseGet(HashMap::new)
        .keySet().stream().collect(Collectors.toList());
  }

  public Map<String, File> getDriverVersionWithPath(ProductTypeEnum dbTypeEnum) {
    return Optional.ofNullable(drivers.get(dbTypeEnum)).orElse(new HashMap<>());
  }

  public File getVersionDriverFile(ProductTypeEnum dbTypeEnum, String driverVersion) {
    return drivers.get(dbTypeEnum).get(driverVersion);
  }

  public List<DatabaseTypeDriverResponse> getDrivers(ProductTypeEnum dbTypeEnum) {
    List<DatabaseTypeDriverResponse> lists = new ArrayList<>();
    getDriverVersionWithPath(dbTypeEnum)
        .forEach(
            (k, v) ->
                lists.add(
                    DatabaseTypeDriverResponse.builder()
                        .driverVersion(k)
                        .driverClass(dbTypeEnum.getDriver())
                        .driverPath(v.getAbsolutePath())
                        .jarFiles(
                            FileUtil.listFileNames(v.getAbsolutePath())
                        )
                        .build()
                )
        );
    return lists;
  }
}
