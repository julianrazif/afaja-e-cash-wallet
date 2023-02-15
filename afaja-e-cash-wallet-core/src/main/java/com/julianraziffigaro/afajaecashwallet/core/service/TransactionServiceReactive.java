package com.julianraziffigaro.afajaecashwallet.core.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import reactor.core.publisher.Mono;

public interface TransactionServiceReactive {

  Mono<TransactionDetails> save(TransactionDomain transactionDomain);
}
