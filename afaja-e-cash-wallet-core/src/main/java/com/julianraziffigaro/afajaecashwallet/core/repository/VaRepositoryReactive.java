package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

public interface VaRepositoryReactive {

  Flux<VaDetails> findByVaNumber(String vaNumber);

  Flux<VaDetails> findByParentVa(String parentVa);

  Flux<VaDetails> findByPhoneNumber(String phoneNumber);

  Flux<VaDetails> save(String vaNumber, String parentVa, String realName, String phoneNumber,
                       BigDecimal currentBalance, String hashedCode);
}
