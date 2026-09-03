package com.es.elsdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class GraphQLConfig {
    private static final String file = "/graphql.properties";

    @Bean(name = "genericGraphQlProperties")
    public Properties getGenericGraphQlProperties() throws IOException {
        return PropertiesLoaderUtils.loadProperties(new ClassPathResource(file));
    }
}
