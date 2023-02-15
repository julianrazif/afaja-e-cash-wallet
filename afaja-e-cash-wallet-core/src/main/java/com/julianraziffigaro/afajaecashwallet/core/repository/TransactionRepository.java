package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public interface TransactionRepository {

  Optional<String> save(Long trxId, String issuedBy, Date issuedDate,
                        String transactionType, BigDecimal amount, String hashedCode);

  Stream<TransactionDetails> findByTrxId(Long trxId);
}
