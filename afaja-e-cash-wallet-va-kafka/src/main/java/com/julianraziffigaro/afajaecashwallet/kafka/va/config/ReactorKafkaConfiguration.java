package com.julianraziffigaro.afajaecashwallet.kafka.va.config;

import com.julianraziffigaro.afajaecashwallet.core.config.KafkaConsumerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaProducerConfigData;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.MicrometerConsumerListener;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class ReactorKafkaConfiguration {

  private final KafkaConsumerConfigData kafkaConsumerConfigData;
  private final KafkaProducerConfigData kafkaProducerConfigData;

  public ReactorKafkaConfiguration(KafkaConsumerConfigData kafkaConsumerConfigData,
                                   KafkaProducerConfigData kafkaProducerConfigData) {
    this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    this.kafkaProducerConfigData = kafkaProducerConfigData;
  }

  @Bean
  public OSHostName getOsHostName() {
    return new OSHostName(kafkaConsumerConfigData);
  }

  @Bean
  public ReceiverOptions<Long, String> receiverOptions() {
    Map<String, Object> consumerProps = new HashMap<>();
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfigData.getBootstrapServers());
    consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, getOsHostName().clientIdWrapper());
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfigData.getGroupId());
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());

    return ReceiverOptions.<Long, String>create(consumerProps)
      .subscription(Collections.singletonList(kafkaConsumerConfigData.getTransactionVaTopic()));
  }

  @Bean
  KafkaReceiver<Long, String> kafkaReceiver() {
    return KafkaReceiver.create(receiverOptions());
  }

  @Bean
  public SenderOptions<Long, String> senderOptions() {
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfigData.getBootstrapServers());
    producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, getOsHostName().clientIdWrapper());
    producerProps.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializer());
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializer());

    return SenderOptions.create(producerProps);
  }

  @Bean
  public KafkaSender<Long, String> kafkaSender() {
    return KafkaSender.create(senderOptions());
  }

  @Bean
  public ConcurrentMap<String, Sinks.Many<String>> sinksMap() {
    return new ConcurrentHashMap<>();
  }

  @Bean
  public MicrometerConsumerListener<Long, String> consumerListener(MeterRegistry registry) {
    return new MicrometerConsumerListener<>(registry);
  }
}
