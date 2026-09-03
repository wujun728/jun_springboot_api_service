package com.zapi.dataset.support.factory;


import com.zapi.dataset.support.service.DatabaseExportService;
import com.zapi.dataset.support.service.DmExportService;
import com.zapi.dataset.support.service.MySQLExportService;

public class DatabaseExportServiceFactory {
    public static DatabaseExportService getService(String dbType) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySQLExportService();
            case "dm":
                return new DmExportService();
            // 添加其他数据库支持
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}