package com.zapi.dataset.support.service;


public class DatabaseMetaServiceFactory {
    public static DatabaseMetaService getService(String dbType) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySQLMetaService();
            case "dm":
                return new DmMetaService();
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}