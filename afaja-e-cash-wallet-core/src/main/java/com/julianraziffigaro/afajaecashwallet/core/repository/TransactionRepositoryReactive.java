package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

public interface TransactionRepositoryReactive {

  Mono<String> save(Long trxId, String issuedBy, Date issuedDate, String transactionType,
                    BigDecimal amount, String hashedCode);

  Flux<TransactionDetails> findByTrxId(Long trxId);
}
