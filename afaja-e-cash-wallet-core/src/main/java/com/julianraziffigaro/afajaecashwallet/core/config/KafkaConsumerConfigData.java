package com.julianraziffigaro.afajaecashwallet.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-consumer-config")
public class KafkaConsumerConfigData {

  private String bootstrapServers;
  private String clientId;
  private String groupId;
  private String keyDeserializer;
  private String valueDeserializer;
  private String autoOffsetReset;
  private String transactionVaTopic;
  private String vaTransactionTopic;
  private String clientCode;
}
