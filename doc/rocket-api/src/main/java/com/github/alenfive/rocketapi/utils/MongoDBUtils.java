package com.github.alenfive.rocketapi.utils;

import com.github.alenfive.rocketapi.entity.DBConfig;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class MongoDBUtils {
    public static MongoTemplate getMongoTemplate(DBConfig config){
        MongoDatabaseFactory mongoDbFactory = new SimpleMongoClientDatabaseFactory(config.getUrl());
        return new MongoTemplate(mongoDbFactory);
    }
}
