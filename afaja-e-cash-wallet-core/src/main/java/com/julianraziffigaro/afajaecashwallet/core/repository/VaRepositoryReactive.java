package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface VaRepositoryReactive {

  Flux<VaDetails> findByVaNumber(String vaNumber);

  Flux<VaDetails> findByParentVa(String parentVa);

  Flux<VaDetails> findByPhoneNumber(String phoneNumber);

  Mono<String> save(String vaNumber, String parentVa, String realName, String phoneNumber,
                    BigDecimal currentBalance, String hashedCode);

  Mono<String> debitCredit(String vaNumber, BigDecimal amount);
}
