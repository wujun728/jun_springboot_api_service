package com.yatop.lambda.net.websocket.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Hazelcast {
    static private String mapName = "hazelcast-map";
    static private HazelcastInstance instance;


    // 如果用户想直接hazelcast提供的方法可以在获取instance后直接使用
    static public HazelcastInstance getInstance() {
        if (instance != null) {
            return instance;
        }

        String instanceName = "hazelInstance_";
        Config config = new Config();
        config.setClusterName("");
        config.setInstanceName(instanceName);
        config.getNetworkConfig().getJoin().getAutoDetectionConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(false);

        // map config
        MapConfig mapConfig = new MapConfig(mapName);
        mapConfig.setBackupCount(2); // 默认的map设置为2副本
        config.addMapConfig(mapConfig);

        instance = com.hazelcast.core.Hazelcast.newHazelcastInstance(config);
        return instance;
    }

    static public <K, V> IMap<K, V> getMap() {
        return instance.getMap(mapName);
    }

    static public <K, V> IMap<K, V> getMap(String name) {
        return instance.getMap(name);
    }

}
