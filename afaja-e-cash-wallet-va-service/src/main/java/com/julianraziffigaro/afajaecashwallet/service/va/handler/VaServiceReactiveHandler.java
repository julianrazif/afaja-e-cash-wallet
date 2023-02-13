package com.julianraziffigaro.afajaecashwallet.service.va.handler;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.service.va.service.VaServiceIntegratorImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class VaServiceReactiveHandler {

  private final VaServiceIntegratorImpl vaServiceIntegrator;

  public VaServiceReactiveHandler(VaServiceIntegratorImpl vaServiceIntegrator) {
    this.vaServiceIntegrator = vaServiceIntegrator;
  }

  public Mono<ServerResponse> inquiryVaUser(ServerRequest request) {
    Mono<VaDetails> sources = request
      .bodyToMono(VaDomain.class)
      .flatMap(vaServiceIntegrator::inquiryVaUserReactive);

    Mono<VaDomain> vaDetailsMono = vaDetailsToDomain(sources);

    return ServerResponse
      .ok()
      .body(BodyInserters.fromPublisher(vaDetailsMono, VaDomain.class));
  }

  private Mono<VaDomain> vaDetailsToDomain(Mono<VaDetails> sources) {
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
