package com.julianraziffigaro.afajaecashwallet.service.va.kafka;

import com.julianraziffigaro.afajaecashwallet.core.kafka.processor.ReactorKafkaProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner {

  private final ReactorKafkaProcessor reactorKafkaProcessor;

  public KafkaRunner(@Qualifier("vaReactorKafkaProcessorImpl") ReactorKafkaProcessor reactorKafkaProcessor) {
    this.reactorKafkaProcessor = reactorKafkaProcessor;
  }

  public void run() {
    this.reactorKafkaProcessor.processor();
  }
}
