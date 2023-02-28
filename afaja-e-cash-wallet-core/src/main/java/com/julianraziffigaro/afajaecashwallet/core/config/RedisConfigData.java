package com.julianraziffigaro.afajaecashwallet.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "redis-config")
public class RedisConfigData {

  private Integer dnsMonitoringInterval = 10000;
  private Integer idleConnectionTimeout = 10000;
  private Integer connectTimeout = 60000;
  private Integer timeout = 60000;
  private Integer retryAttempts = 30;
  private Integer retryInterval = 1500;
  private String password;
  private Integer subscriptionsPerConnection = 50;
  private Integer pingConnectionInterval = 60000;
  private String clientName;
  private String address = "redis://10.197.50.78:6379";
  private Integer subscriptionConnectionMinimumIdleSize = 1;
  private Integer subscriptionConnectionPoolSize = 100;
  private Integer connectionMinimumIdleSize = 24;
  private Integer connectionPoolSize = 6400;
  private Integer database = 7;
  private Boolean keepAlive = false;
  private Boolean tcpNoDelay = false;
  private Integer LockWatchdogTimeout = 10000;
  private Integer threads = 256;
  private Integer nettyThreads = 512;
}
