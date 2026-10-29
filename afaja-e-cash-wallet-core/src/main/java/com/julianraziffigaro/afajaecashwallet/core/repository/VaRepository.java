package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

public interface VaRepository {

  Stream<VaDetails> findByVaNumber(String vaNumber);

  Stream<VaDetails> findByParentVa(String parentVa);

  Stream<VaDetails> findByPhoneNumber(String phoneNumber);

  Optional<String> save(String vaNumber, String parentVa, String realName, String phoneNumber,
                BigDecimal currentBalance, String hashedCode);

  Optional<String> debitCredit(String vaNumber, BigDecimal amount);
}
