package com.julianraziffigaro.afajaecashwallet.kafka.transaction.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaConsumerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaProducerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.kafka.processor.ReactorKafkaProcessor;
import com.julianraziffigaro.afajaecashwallet.core.model.KafkaMessage;
import com.julianraziffigaro.afajaecashwallet.kafka.transaction.config.OSHostName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionReactorKafkaProcessorImpl implements ReactorKafkaProcessor {

  private final OSHostName osHostName;
  private final KafkaConsumerConfigData kafkaConsumerConfigData;
  private final KafkaProducerConfigData kafkaProducerConfigData;
  private final KafkaReceiver<Long, String> kafkaReceiver;
  private final KafkaSender<Long, String> kafkaSender;
  private final MicrometerConsumerListener<Long, String> consumerListener;

  @Override
  public void processor() {
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
      })
      .subscribe(
        kafkaRecord -> {
          log.info("Received Data : {}", kafkaRecord.value());

          KafkaMessage message = new KafkaMessage();

          try {
            message = new ObjectMapper().readValue(kafkaRecord.value(), KafkaMessage.class);
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }

          final KafkaMessage finalMessage = message;
          finalMessage.setKey(UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE);

          kafkaRecord.receiverOffset().acknowledge();

          Flux<KafkaMessage> messageValue;

          if (finalMessage.getMethod().equals("TransactionDebitCredit")) {
            log.info("debit credit");
            messageValue = Flux.empty();
          } else {
            log.info("No Such Method as : {}", finalMessage.getMethod());
            return;
          }

          Flux
            .defer(
              () -> kafkaSender
                .send(createOutBoundFlux(endSignalMessage(finalMessage)))
                .then()
                .delayElement(Duration.ofMillis(50))
                .doOnError(e -> log.error("Send end signal failed", e))
                .doOnSuccess(s -> log.info("Send end signal succeeded"))
                .delaySubscription(
                  Flux.defer(
                      () -> kafkaSender
                        .send(createOutBoundFlux(messageValue))
                        .then()
                        .doOnError(e -> log.error("Send failed", e))
                        .doOnSuccess(ss -> log.info("Send succeeded"))
                    )
                    .subscribeOn(Schedulers.boundedElastic())
                )
            )
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe();
        });

    kafkaReceiver.doOnConsumer(consumer -> {
      consumerListener.consumerAdded(osHostName.clientIdWrapper(), consumer);
      return Mono.empty();
    }).subscribe();
  }

  private Flux<SenderRecord<Long, String, Long>> createOutBoundFlux(Flux<KafkaMessage> messageValue) {
    return messageValue
      .map(i -> {
        ProducerRecord<Long, String> producerRecord = new ProducerRecord<>(kafkaProducerConfigData.getTransactionVaTopic(), i.getKey(), new Gson().toJson(i));
        SenderRecord<Long, String, Long> kafkaRecord = SenderRecord.create(producerRecord, i.getKey());
        log.info("sent key={}, data={}", kafkaRecord.key(), kafkaRecord.value());
        return kafkaRecord;
      });
  }

  private Flux<KafkaMessage> endSignalMessage(KafkaMessage finalMessage) {
    return Flux.just(new KafkaMessage(finalMessage.getKey(), finalMessage.getClientCode(), "End Signal", finalMessage.getMessageId(), 0, null));
  }
}
