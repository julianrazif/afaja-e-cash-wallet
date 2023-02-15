package com.julianraziffigaro.afajaecashwallet.core.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;

import java.util.Optional;

public interface TransactionService {

  Optional<TransactionDetails> save(TransactionDomain transactionDomain);
}
