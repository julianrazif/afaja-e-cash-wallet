package com.julianraziffigaro.afajaecashwallet.reactive.va.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
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
    return this.vaRepository.save(
      vaDomain.getVaNumber(),
      vaDomain.getParentVa(),
      vaDomain.getRealName(),
      vaDomain.getPhoneNumber(),
      vaDomain.getCurrentBalance(),
      vaDomain.getHashedCode()
    ).singleOrEmpty();
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
