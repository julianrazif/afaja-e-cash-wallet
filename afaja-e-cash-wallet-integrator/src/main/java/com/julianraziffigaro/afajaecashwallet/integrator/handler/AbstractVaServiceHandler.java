package com.julianraziffigaro.afajaecashwallet.integrator.handler;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.integrator.service.VaServiceIntegratorImpl;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class AbstractVaServiceHandler {

  private final VaServiceIntegratorImpl vaServiceIntegrator;

  protected AbstractVaServiceHandler(VaServiceIntegratorImpl vaServiceIntegrator) {
    this.vaServiceIntegrator = vaServiceIntegrator;
  }

  protected VaServiceIntegratorImpl getVaServiceIntegratorImpl() {
    return this.vaServiceIntegrator;
  }

  protected abstract Mono<ServerResponse> inquiryVaUser(ServerRequest request);

  protected Mono<VaDomain> vaDetailsToDomain(Mono<VaDetails> sources) {
    return sources
      .map(vaDetails -> {
        VaDomain va = new VaDomain();
        va.setVaNumber(vaDetails.getVaNumber());
        va.setParentVa(vaDetails.getParentVa());
        va.setRealName(vaDetails.getRealName());
        va.setPhoneNumber(vaDetails.getPhoneNumber());
        va.setCurrentBalance(vaDetails.getCurrentBalance());
        va.setHashedCode(vaDetails.getHashedCode());
        return va;
      })
      .log();
  }
}
