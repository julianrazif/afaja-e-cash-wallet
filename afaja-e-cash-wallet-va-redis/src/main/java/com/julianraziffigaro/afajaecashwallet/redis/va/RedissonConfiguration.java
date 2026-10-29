package com.julianraziffigaro.afajaecashwallet.redis.va;

import com.julianraziffigaro.afajaecashwallet.core.config.RedisConfigData;
import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedissonConfiguration {

  private final RedisConfigData redisConfigData;

  @Primary
  @Bean(name = "myRedissonConnectionFactory")
  public RedissonConnectionFactory redissonConnectionFactory() {
    Config config = new Config();
    config.useSingleServer()
      .setDnsMonitoringInterval(redisConfigData.getDnsMonitoringInterval())
      .setIdleConnectionTimeout(redisConfigData.getIdleConnectionTimeout())
      .setConnectTimeout(redisConfigData.getConnectTimeout())
      .setTimeout(redisConfigData.getTimeout())
      .setRetryAttempts(redisConfigData.getRetryAttempts())
      .setRetryInterval(redisConfigData.getRetryInterval())
      .setDatabase(redisConfigData.getDatabase())
      .setPassword(redisConfigData.getPassword())
      .setSubscriptionsPerConnection(redisConfigData.getSubscriptionsPerConnection())
      .setPingConnectionInterval(redisConfigData.getPingConnectionInterval())
      .setClientName(redisConfigData.getClientName())
      .setAddress(redisConfigData.getAddress())
      .setSubscriptionConnectionMinimumIdleSize(redisConfigData.getSubscriptionConnectionMinimumIdleSize())
      .setSubscriptionConnectionPoolSize(redisConfigData.getSubscriptionConnectionPoolSize())
      .setConnectionPoolSize(redisConfigData.getConnectionPoolSize())
      .setConnectionMinimumIdleSize(redisConfigData.getConnectionMinimumIdleSize())
      .setKeepAlive(redisConfigData.getKeepAlive())
      .setTcpNoDelay(redisConfigData.getTcpNoDelay());

    config.setLockWatchdogTimeout(redisConfigData.getLockWatchdogTimeout())
      .setCodec(new JsonJacksonCodec())
      .setThreads(redisConfigData.getThreads())
      .setNettyThreads(redisConfigData.getNettyThreads())
      .setTransportMode(TransportMode.NIO);

    RedissonClient redissonClient = Redisson.create(config);

    return new RedissonConnectionFactory(redissonClient);
  }

  @Bean
  public ReactiveRedisTemplate<String, VaDomain> reactiveJsonPostRedisTemplate(@Qualifier("myRedissonConnectionFactory") ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    RedisSerializationContext<String, VaDomain> serializationContext = RedisSerializationContext
      .<String, VaDomain>newSerializationContext(new StringRedisSerializer())
      .hashKey(new StringRedisSerializer())
      .hashValue(new Jackson2JsonRedisSerializer<>(VaDomain.class))
      .build();

    return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
  }
}
