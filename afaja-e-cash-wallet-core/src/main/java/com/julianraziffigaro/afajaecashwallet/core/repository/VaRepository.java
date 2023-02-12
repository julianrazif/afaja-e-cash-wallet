package com.julianraziffigaro.afajaecashwallet.core.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface VaRepository {

  Stream<VaDetails> findByVaNumber(String vaNumber);

  Stream<VaDetails> findByParentVa(String parentVa);

  Stream<VaDetails> findByPhoneNumber(String phoneNumber);

  Stream<VaDetails> save(String vaNumber, String parentVa, String realName, String phoneNumber,
                         BigDecimal currentBalance, String hashedCode);
}
