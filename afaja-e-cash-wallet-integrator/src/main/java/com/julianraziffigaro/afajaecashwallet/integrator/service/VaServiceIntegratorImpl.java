package com.julianraziffigaro.afajaecashwallet.integrator.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.VaService;
import com.julianraziffigaro.afajaecashwallet.core.service.VaServiceReactive;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VaServiceIntegratorImpl {

  private final VaService vaService;
  private final VaServiceReactive vaServiceReactive;

  public VaServiceIntegratorImpl(VaService vaService,
                                 VaServiceReactive vaServiceReactive) {
    this.vaService = vaService;
    this.vaServiceReactive = vaServiceReactive;
  }

  public Mono<VaDetails> inquiryVaUser(VaDomain vaDomain) {
    return Mono.justOrEmpty(vaService.inquiry(vaDomain));
  }

  public Mono<VaDetails> inquiryVaUserReactive(VaDomain vaDomain) {
    return vaServiceReactive.inquiry(vaDomain);
  }

  public Optional<VaDetails> createVaUser(VaDomain vaDomain) {
    return vaService.save(vaDomain);
  }

  public Stream<VaDetails> listVaUser(VaDomain vaDomain) {
    return vaService.allByParent(vaDomain);
  }
}
