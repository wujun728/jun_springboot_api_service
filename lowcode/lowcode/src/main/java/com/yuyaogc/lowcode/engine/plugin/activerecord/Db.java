package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.util.HashMap;
import java.util.Map;

public final class Db {
    static Config config = null;
    public static final String MAIN_CONFIG_NAME = "main";
    private static Map<String, Config> dbNameToDb = new HashMap<String, Config>(32, 0.25F);

    private Db() {
    }

    public static void addDb(Config db) {
        if (db == null) {
            throw new IllegalArgumentException("ActiveRecord can not be null");
        }
        if (dbNameToDb.containsKey(db.getName())) {
            throw new IllegalArgumentException("ActiveRecord already exists: " + db.getName());
        }

        dbNameToDb.put(db.getName(), db);

        /**
         * Replace the main config if current config name is MAIN_CONFIG_NAME
         */
        if (MAIN_CONFIG_NAME.equals(db.getName())) {
            Db.config = db;
            //TODO 工厂类
        }
        // TODO 多数据源
    }

    public static Config removeDb(String dbName) {
        if (Db.config != null && Db.config.getName().equals(dbName)) {
            // throw new RuntimeException("Can not remove the main config.");
            Db.config = null;
        }
        return dbNameToDb.remove(dbName);
    }

    public static Config getConfig() {
        return dbNameToDb.get("master");
    }

    public static Config getConfig(String dbName) {
        return dbNameToDb.get(dbName);
    }

}
