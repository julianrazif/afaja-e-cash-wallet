package com.julianraziffigaro.afajaecashwallet.service.transaction.kafka;

import com.julianraziffigaro.afajaecashwallet.core.kafka.consumer.KafkaReceiverTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner {

  private final KafkaReceiverTemplate kafkaReceiverTemplate;

  public KafkaRunner(@Qualifier("transactionKafkaReceiverTemplateImpl") KafkaReceiverTemplate kafkaReceiverTemplate) {
    this.kafkaReceiverTemplate = kafkaReceiverTemplate;
  }

  public void run() {
    this.kafkaReceiverTemplate.receiver();
  }
}
