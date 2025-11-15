package com.frauas.workforce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.frauas.workforce.repository")
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB auto-configuration will handle the rest
}
