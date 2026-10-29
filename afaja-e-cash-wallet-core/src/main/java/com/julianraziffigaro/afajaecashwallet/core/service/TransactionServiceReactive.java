package com.julianraziffigaro.afajaecashwallet.core.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import reactor.core.publisher.Mono;

import java.text.ParseException;

public interface TransactionServiceReactive {

  Mono<TransactionDetails> save(TransactionDomain transactionDomain) throws ParseException;
}
