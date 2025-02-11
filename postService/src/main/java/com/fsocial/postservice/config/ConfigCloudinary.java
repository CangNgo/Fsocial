package com.fsocial.postservice.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map<String, String> config = new HashMap();
        config.put("cloud_name", "dwf2vqohm");
        config.put("api_key", "736994548623143");
        config.put("api_secret", "8SVWLYHMQW1W9BDrA_jRztIICKI");
        return new Cloudinary(config);
    }
}
