package com.julianraziffigaro.afajaecashwallet.service.transaction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianraziffigaro.afajaecashwallet.core.config.KafkaConsumerConfigData;
import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.kafka.producer.KafkaSenderTemplate;
import com.julianraziffigaro.afajaecashwallet.core.model.KafkaMessage;
import com.julianraziffigaro.afajaecashwallet.core.service.TransactionServiceReactive;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.UUID;

@Service
public class TransactionServiceIntegratorImpl {

  private final TransactionServiceReactive transactionServiceReactive;
  private final KafkaConsumerConfigData kafkaConsumerConfigData;

  private final KafkaSenderTemplate kafkaSenderTemplate;

  public TransactionServiceIntegratorImpl(TransactionServiceReactive transactionServiceReactive,
                                          KafkaConsumerConfigData kafkaConsumerConfigData,
                                          @Qualifier("transactionKafkaSenderTemplateImpl") KafkaSenderTemplate kafkaSenderTemplate) {
    this.transactionServiceReactive = transactionServiceReactive;
    this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    this.kafkaSenderTemplate = kafkaSenderTemplate;
  }

  public Flux<VaDomain> submitTransaction(TransactionDomain transactionDomain) throws ParseException {
    String method = "Submit Transaction";
    Long key = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    String messageId = method + key;

    return Flux
      .from(this.transactionServiceReactive.save(transactionDomain))
      .flatMap(
        transactionDetails -> kafkaSenderTemplate
          .sendReactiveRequestReply(
            new KafkaMessage(
              key,
              kafkaConsumerConfigData.getClientCode(),
              method,
              messageId,
              0,
              new TransactionDomain(
                transactionDetails.getTrxId(),
                new VaDomain(
                  transactionDetails.getIssuedBy().getVaNumber(),
                  transactionDetails.getIssuedBy().getParentVa(),
                  transactionDetails.getIssuedBy().getRealName(),
                  transactionDetails.getIssuedBy().getPhoneNumber(),
                  transactionDetails.getIssuedBy().getCurrentBalance(),
                  transactionDetails.getIssuedBy().getHashedCode()
                ),
                DateFormat.getDateInstance().format(transactionDetails.getIssuedDate()),
                transactionDetails.getTransactionType(),
                transactionDetails.getAmount(),
                transactionDetails.getHashedCode()
              )
            )
          )
      )
      .filter(result -> result.getEc() == 0)
      .flatMap(result -> Flux.just(new ObjectMapper().convertValue(result.getData(), VaDomain.class)));
  }
}
