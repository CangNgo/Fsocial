package com.fsocial.postservice.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Slf4j
@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.username}")
    private String username;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.ssl.enabled}")
    private boolean sslEnable;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        log.info("Configuring Redis connection to {}: {}", host, port);
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(Integer.parseInt(port));
        redisConfig.setPassword(password);
        if(username != null && !username.isEmpty()){
            redisConfig.setUsername(username);
            log.info("Redis username is {}", username);
        }
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(10))
                .keepAlive(true)
                .build();
        ClientOptions clientOptions = ClientOptions.builder()
                .socketOptions(socketOptions)
                .autoReconnect(true)
                .build();

        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigBuilder =  LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(5))
                .clientOptions(clientOptions);

        if (sslEnable) {
            clientConfigBuilder.useSsl().disablePeerVerification(); // Disable peer verification for cloud Redis
            log.info("🔒 SSL enabled for Redis connection");
        }

        LettuceClientConfiguration clientConfig = clientConfigBuilder.build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
        // Không validate connection khi startup để tránh block application nếu Redis chưa sẵn sàng
        factory.setValidateConnection(false);
        // Không gọi afterPropertiesSet() ở đây để tránh validate connection ngay lập tức
        
        log.info("✅ Redis connection factory created successfully (lazy connection)");
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("🔧 Creating RedisTemplate bean");

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializers
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value serializers
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.setEnableTransactionSupport(false);
        // Không gọi afterPropertiesSet() để tránh validate connection ngay lập tức
        // Connection sẽ được validate khi sử dụng lần đầu tiên

        log.info("✅ RedisTemplate bean created successfully (lazy connection)");
        return template;
    }
    
    @Bean
    public org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("🔧 Creating StringRedisTemplate bean");
        org.springframework.data.redis.core.StringRedisTemplate template = new org.springframework.data.redis.core.StringRedisTemplate(connectionFactory);
        log.info("✅ StringRedisTemplate bean created successfully");
        return template;
    }

}
