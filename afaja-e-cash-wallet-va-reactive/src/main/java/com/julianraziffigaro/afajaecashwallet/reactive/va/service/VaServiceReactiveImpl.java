package com.julianraziffigaro.afajaecashwallet.reactive.va.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionType;
import com.julianraziffigaro.afajaecashwallet.core.model.Va;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.VaServiceReactive;
import com.julianraziffigaro.afajaecashwallet.reactive.va.repository.VaRepositoryReactiveImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VaServiceReactiveImpl implements VaServiceReactive {

  private final VaRepositoryReactiveImpl vaRepository;

  public VaServiceReactiveImpl(VaRepositoryReactiveImpl vaRepository) {
    this.vaRepository = vaRepository;
  }

  @Override
  public Mono<VaDetails> save(VaDomain vaDomain) {
    return inquiry(vaDomain)
      .switchIfEmpty(
        this.vaRepository
          .save(
            vaDomain.getVaNumber(),
            vaDomain.getParentVa(),
            vaDomain.getRealName(),
            vaDomain.getPhoneNumber(),
            vaDomain.getCurrentBalance(),
            vaDomain.getHashedCode()
          )
          .flatMap(vaNumber -> this.vaRepository.findByVaNumber(vaNumber).singleOrEmpty())
      )
      .flatMap(vaDetails -> Mono.empty());
  }

  @Override
  public Mono<VaDetails> debitCredit(TransactionDomain transactionDomain) {
    return inquiry(transactionDomain.getIssuedBy())
      .switchIfEmpty(Mono.empty())
      .flatMap(vaDetails -> {
        VaDetails va;
        if (transactionDomain.getTransactionType().equals(TransactionType.DEBIT)) {
          va = Va.withVaDetails(vaDetails).withDebit(transactionDomain.getAmount()).build();
        } else if (transactionDomain.getTransactionType().equals(TransactionType.CREDIT)) {
          va = Va.withVaDetails(vaDetails).withCredit(transactionDomain.getAmount()).build();
        } else {
          return Mono.empty();
        }
        return this.vaRepository.debitCredit(va.getVaNumber(), va.getCurrentBalance());
      })
      .flatMap(vaNumber -> this.vaRepository.findByVaNumber(vaNumber).singleOrEmpty());
  }

  @Override
  public Mono<VaDetails> inquiry(VaDomain vaDomain) {
    String vaNumber = vaDomain.getVaNumber();
    String phoneNumber = vaDomain.getPhoneNumber();

    if (vaNumber != null) {
      return this.vaRepository.findByVaNumber(vaNumber).singleOrEmpty();
    } else if (phoneNumber != null) {
      return this.vaRepository.findByPhoneNumber(phoneNumber).singleOrEmpty();
    } else {
      return Mono.empty();
    }
  }

  @Override
  public Flux<VaDetails> allByParent(VaDomain vaDomain) {
    String parentVa = vaDomain.getParentVa();

    if (parentVa != null) {
      return this.vaRepository.findByParentVa(parentVa);
    } else {
      return Flux.empty();
    }
  }
}
