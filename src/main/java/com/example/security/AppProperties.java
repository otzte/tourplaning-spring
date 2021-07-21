package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Autowired
    private Environment environment;

    @Bean
    public String getTokenSecret(){
        return environment.getProperty("tokenSecret");
    }
}
