package com.julianraziffigaro.afajaecashwallet.core.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;

import java.util.Optional;
import java.util.stream.Stream;

public interface VaService {

  Optional<VaDetails> save(VaDomain vaDomain);

  Optional<VaDetails> inquiry(VaDomain vaDomain);

  Stream<VaDetails> allByParent(VaDomain vaDomain);
}
