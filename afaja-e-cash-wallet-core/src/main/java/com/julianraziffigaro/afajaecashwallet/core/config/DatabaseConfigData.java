package com.julianraziffigaro.afajaecashwallet.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "db-postgres")
public class DatabaseConfigData {

  private String url;
  private String username;
  private String password;
  private String schema;
}
