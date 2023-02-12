package com.julianraziffigaro.afajaecashwallet.core.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VaServiceReactive {

  Mono<VaDetails> save(VaDomain vaDomain);

  Mono<VaDetails> inquiry(VaDomain vaDomain);

  Flux<VaDetails> allByParent(VaDomain vaDomain);
}
