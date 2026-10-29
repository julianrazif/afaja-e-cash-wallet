package com.julianraziffigaro.afajaecashwallet.transaction.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.TransactionService;
import com.julianraziffigaro.afajaecashwallet.transaction.repository.TransactionRepositoryImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepositoryImpl transactionRepository;

  public TransactionServiceImpl(TransactionRepositoryImpl transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Optional<TransactionDetails> save(TransactionDomain transactionDomain) throws ParseException {
    return this.transactionRepository.save(
      null,
      transactionDomain.getIssuedBy().getVaNumber(),
      new SimpleDateFormat("yyyy-MM-dd").parse(transactionDomain.getIssuedDate()),
      transactionDomain.getTransactionType().name(),
      transactionDomain.getAmount(),
      transactionDomain.getHashedCode()
    ).flatMap(trxId -> this.transactionRepository.findByTrxId(Long.valueOf(trxId)).findFirst());
  }
}
