package com.julianraziffigaro.afajaecashwallet.reactive.transaction.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.TransactionServiceReactive;
import com.julianraziffigaro.afajaecashwallet.reactive.transaction.repository.TransactionRepositoryReactiveImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class TransactionServiceReactiveImpl implements TransactionServiceReactive {

  private final TransactionRepositoryReactiveImpl transactionRepositoryReactive;

  public TransactionServiceReactiveImpl(TransactionRepositoryReactiveImpl transactionRepositoryReactive) {
    this.transactionRepositoryReactive = transactionRepositoryReactive;
  }

  @Override
  public Mono<TransactionDetails> save(TransactionDomain transactionDomain) throws ParseException {
    return this.transactionRepositoryReactive
      .save(
        null,
        transactionDomain.getIssuedBy().getVaNumber(),
        new SimpleDateFormat("yyyy-MM-dd").parse(transactionDomain.getIssuedDate()),
        transactionDomain.getTransactionType().name(),
        transactionDomain.getAmount(),
        transactionDomain.getHashedCode()
      )
      .flatMap(trxId -> this.transactionRepositoryReactive.findByTrxId(Long.valueOf(trxId)).singleOrEmpty());
  }
}
