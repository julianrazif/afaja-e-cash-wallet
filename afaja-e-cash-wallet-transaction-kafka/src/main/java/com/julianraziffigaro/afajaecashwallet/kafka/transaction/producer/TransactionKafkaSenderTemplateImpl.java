package com.julianraziffigaro.afajaecashwallet.kafka.transaction.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaProducerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.kafka.producer.KafkaSenderTemplate;
import com.julianraziffigaro.afajaecashwallet.core.model.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionKafkaSenderTemplateImpl implements KafkaSenderTemplate {

  private final KafkaProducerConfigData kafkaProducerConfigData;
  private final KafkaSender<Long, String> kafkaSender;
  private final Map<String, Sinks.Many<String>> sinksMap;

  @Override
  public Flux<KafkaMessage> sendReactiveRequestReply(KafkaMessage messageValue) {
    sinksMap.put(messageValue.getMessageId(), Sinks.many().multicast().onBackpressureBuffer(1));

    Flux<KafkaMessage> objectFlux = sinksMap.get(messageValue.getMessageId())
      .asFlux()
      .filter(s -> {
        KafkaMessage message = new KafkaMessage();

        try {
          message = new ObjectMapper().readValue(s, KafkaMessage.class);
        } catch (JsonProcessingException ex) {
          ex.printStackTrace();
        }

        return messageValue.getMessageId().equals(message.getMessageId());
      })
      .map(kafkaRecord -> {
        KafkaMessage message = new KafkaMessage();

        try {
          message = new ObjectMapper().readValue(kafkaRecord, KafkaMessage.class);
        } catch (JsonProcessingException ex) {
          ex.printStackTrace();
        }

        return message;
      });

    return send(createOutBoundFlux(messageValue)).doOnError(e -> log.error("Send failed", e)).thenMany(objectFlux);
  }

  private Flux<SenderResult<Long>> send(Flux<SenderRecord<Long, String, Long>> recordFlux) {
    return kafkaSender.send(recordFlux);
  }

  private Flux<SenderRecord<Long, String, Long>> createOutBoundFlux(KafkaMessage messageValue) {
    return Flux.just(messageValue)
      .map(i -> {
        ProducerRecord<Long, String> producerRecord = new ProducerRecord<>(kafkaProducerConfigData.getTransactionVaTopic(), i.getKey(), new Gson().toJson(i));
        SenderRecord<Long, String, Long> kafkaRecord = SenderRecord.create(producerRecord, i.getKey());
        log.info("Sent Data --> {}, topic={}", kafkaRecord.value(), kafkaProducerConfigData.getTransactionVaTopic());
        return kafkaRecord;
      });
  }
}
