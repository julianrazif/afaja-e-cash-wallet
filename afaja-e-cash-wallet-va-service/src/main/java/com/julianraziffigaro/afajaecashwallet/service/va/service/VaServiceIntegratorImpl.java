package com.julianraziffigaro.afajaecashwallet.service.va.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.VaServiceReactive;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VaServiceIntegratorImpl {

  private final VaServiceReactive vaServiceReactive;

  public VaServiceIntegratorImpl(VaServiceReactive vaServiceReactive) {
    this.vaServiceReactive = vaServiceReactive;
  }

  public Mono<VaDetails> inquiryVaUserReactive(VaDomain vaDomain) {
    return vaServiceReactive.inquiry(vaDomain);
  }

  public Flux<VaDetails> allVaUserByParentReactive(VaDomain vaDomain) {
    return vaServiceReactive.allByParent(vaDomain);
  }
}
