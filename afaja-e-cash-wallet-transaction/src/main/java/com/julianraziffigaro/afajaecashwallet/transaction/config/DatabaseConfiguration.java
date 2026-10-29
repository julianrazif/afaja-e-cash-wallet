package com.julianraziffigaro.afajaecashwallet.transaction.config;

import com.julianraziffigaro.afajaecashwallet.core.config.DatabaseConfigData;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class DatabaseConfiguration {

  private final DatabaseConfigData databaseConfigData;

  public DatabaseConfiguration(DatabaseConfigData databaseConfigData) {
    this.databaseConfigData = databaseConfigData;
  }

  @Bean
  public HikariDataSource dataSource() {
    DataSourceBuilder<HikariDataSource> dataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);

    dataSourceBuilder.url(databaseConfigData.getUrl());
    dataSourceBuilder.username(databaseConfigData.getUsername());
    dataSourceBuilder.password(databaseConfigData.getPassword());

    HikariDataSource dataSource = dataSourceBuilder.build();

    dataSource.setConnectionTimeout(TimeUnit.SECONDS.toMillis(10));
    dataSource.setIdleTimeout(TimeUnit.SECONDS.toMillis(1));
    dataSource.setMaxLifetime(TimeUnit.SECONDS.toMillis(30));
    dataSource.setKeepaliveTime(TimeUnit.SECONDS.toMillis(1));
    dataSource.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10));
    dataSource.setMinimumIdle(2);
    dataSource.setMaximumPoolSize(20);
    dataSource.setPoolName("data");
    dataSource.setConnectionTestQuery("select 1");
    dataSource.setAutoCommit(true);
    dataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    dataSource.setSchema(databaseConfigData.getSchema());

    return dataSource;
  }
}
