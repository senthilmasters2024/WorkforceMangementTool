package com.frauas.workforce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.frauas.workforce.repository")
public class MongoConfig {
    // MongoDB auto-configuration will handle the rest
}
