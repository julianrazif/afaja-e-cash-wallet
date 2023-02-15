package com.julianraziffigaro.afajaecashwallet.kafka.va.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaConsumerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.kafka.consumer.KafkaReceiverTemplate;
import com.julianraziffigaro.afajaecashwallet.core.model.KafkaMessage;
import com.julianraziffigaro.afajaecashwallet.kafka.va.config.OSHostName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class VaKafkaReceiverTemplateImpl implements KafkaReceiverTemplate {

  private final OSHostName osHostName;
  private final KafkaConsumerConfigData kafkaConsumerConfigData;
  private final KafkaReceiver<Long, String> kafkaReceiver;
  private final Map<String, Sinks.Many<String>> sinksMap;
  private final MicrometerConsumerListener<Long, String> consumerListener;

  @Override
  public void receiver() {
    kafkaReceiver
      .receive()
      .filter(kafkaRecord -> {
        KafkaMessage message = new KafkaMessage();

        try {
          message = new ObjectMapper().readValue(kafkaRecord.value(), KafkaMessage.class);
        } catch (JsonProcessingException ex) {
          ex.printStackTrace();
        }

        return kafkaConsumerConfigData.getClientCode().equals(message.getClientCode());
      }).subscribe(
        kafkaRecord -> {
          KafkaMessage message = new KafkaMessage();

          try {
            message = new ObjectMapper().readValue(kafkaRecord.value(), KafkaMessage.class);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }

          final KafkaMessage finalMessage = message;

          kafkaRecord.receiverOffset().acknowledge();

          Sinks.EmitResult result;

          log.info("Received Data --> {}", new Gson().toJson(finalMessage));

          if (finalMessage.getMethod().equals("End Signal")) {
            if (null != sinksMap.get(finalMessage.getMessageId())) {
              sinksMap.get(finalMessage.getMessageId()).tryEmitComplete();
              sinksMap.remove(finalMessage.getMessageId());
            }
          } else {
            if (null != sinksMap.get(finalMessage.getMessageId())) {
              result = sinksMap.get(finalMessage.getMessageId()).tryEmitNext(new Gson().toJson(finalMessage));
              if (result.isFailure()) {
                sinksMap.clear();
                log.error(result.name());
              }
            }
          }
        });

    kafkaReceiver.doOnConsumer(consumer -> {
      consumerListener.consumerAdded(osHostName.clientIdWrapper(), consumer);
      return Mono.empty();
    }).subscribe();
  }
}
