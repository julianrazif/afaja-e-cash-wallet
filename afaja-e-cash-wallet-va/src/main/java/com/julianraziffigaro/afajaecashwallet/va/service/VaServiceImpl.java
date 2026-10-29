package com.julianraziffigaro.afajaecashwallet.va.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionType;
import com.julianraziffigaro.afajaecashwallet.core.model.Va;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.VaService;
import com.julianraziffigaro.afajaecashwallet.va.repository.VaRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VaServiceImpl implements VaService {

  private final VaRepositoryImpl vaRepository;

  public VaServiceImpl(VaRepositoryImpl vaRepository) {
    this.vaRepository = vaRepository;
  }

  @Override
  public Optional<VaDetails> save(VaDomain vaDomain) {
    VaDetails vaDetails = inquiry(vaDomain).orElse(null);

    if (vaDetails != null) {
      return Optional.empty();
    }

    return this.vaRepository
      .save(
        vaDomain.getVaNumber(),
        vaDomain.getParentVa(),
        vaDomain.getRealName(),
        vaDomain.getPhoneNumber(),
        vaDomain.getCurrentBalance(),
        vaDomain.getHashedCode()
      ).flatMap(vaNumber -> this.vaRepository.findByVaNumber(vaNumber).findFirst());
  }

  @Override
  public Optional<VaDetails> debitCredit(TransactionDomain transactionDomain) {
    VaDetails vaDetails = inquiry(transactionDomain.getIssuedBy()).orElse(null);

    if (vaDetails == null) {
      return Optional.empty();
    }

    VaDetails va;

    if (transactionDomain.getTransactionType().equals(TransactionType.DEBIT)) {
      va = Va.withVaDetails(vaDetails).withDebit(transactionDomain.getAmount()).build();
    } else if (transactionDomain.getTransactionType().equals(TransactionType.CREDIT)) {
      va = Va.withVaDetails(vaDetails).withCredit(transactionDomain.getAmount()).build();
    } else {
      return Optional.empty();
    }

    return this.vaRepository
      .debitCredit(va.getVaNumber(), va.getCurrentBalance())
      .flatMap(vaNumber -> this.vaRepository.findByVaNumber(vaNumber).findFirst());
  }

  @Override
  public Optional<VaDetails> inquiry(VaDomain vaDomain) {
    String vaNumber = vaDomain.getVaNumber();
    String phoneNumber = vaDomain.getPhoneNumber();

    if (vaNumber != null) {
      return this.vaRepository.findByVaNumber(vaNumber).findFirst();
    } else if (phoneNumber != null) {
      return this.vaRepository.findByPhoneNumber(phoneNumber).findFirst();
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Stream<VaDetails> allByParent(VaDomain vaDomain) {
    String parentVa = vaDomain.getParentVa();

    if (parentVa != null) {
      return this.vaRepository.findByParentVa(parentVa);
    } else {
      return Stream.empty();
    }
  }
}
