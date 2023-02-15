package com.julianraziffigaro.afajaecashwallet.core.kafka.producer;

import com.julianraziffigaro.afajaecashwallet.core.model.KafkaMessage;
import reactor.core.publisher.Flux;

public interface KafkaSenderTemplate {

  Flux<KafkaMessage> sendReactiveRequestReply(KafkaMessage messageValue);
}
