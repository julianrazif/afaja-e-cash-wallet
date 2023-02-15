package com.julianraziffigaro.afajaecashwallet.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaProducerConfigData {

  private String bootstrapServers;
  private String clientId;
  private String keySerializer;
  private String valueSerializer;
  private String acks;
  private String vaTransactionTopic;
  private String transactionVaTopic;
}
