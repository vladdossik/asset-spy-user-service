package asset.spy.user.service.cache.config;

import asset.spy.user.service.cache.model.CacheNames;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.user.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisSerializer<UserResponseDto> userSerializer,
                                          RedisSerializer<ContactResponseDto> contactSerializer) {

        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();

        configurationMap.put(CacheNames.USER, createCacheConfig(userSerializer));
        configurationMap.put(CacheNames.CONTACT, createCacheConfig(contactSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    @Bean
    public RedisSerializer<UserResponseDto> userResponseDtoRedisSerializer(ObjectMapper objectMapper) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, UserResponseDto.class);
    }

    @Bean
    public RedisSerializer<ContactResponseDto> contactResponseDtoRedisSerializer(ObjectMapper objectMapper) {
        return new Jackson2JsonRedisSerializer<>(objectMapper, ContactResponseDto.class);
    }

    private RedisCacheConfiguration createCacheConfig(RedisSerializer<?> serializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }
}
