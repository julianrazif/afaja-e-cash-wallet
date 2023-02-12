package com.julianraziffigaro.afajaecashwallet.integrator.handler;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.integrator.service.VaServiceIntegratorImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class VaServiceReactiveHandler extends AbstractVaServiceHandler {

  public VaServiceReactiveHandler(VaServiceIntegratorImpl vaServiceIntegrator) {
    super(vaServiceIntegrator);
  }

  public Mono<ServerResponse> inquiryVaUser(ServerRequest request) {
    Mono<VaDetails> sources = request
      .bodyToMono(VaDomain.class)
      .flatMap(getVaServiceIntegratorImpl()::inquiryVaUserReactive);

    Mono<VaDomain> vaDetailsMono = vaDetailsToDomain(sources);

    return ServerResponse
      .ok()
      .body(BodyInserters.fromPublisher(vaDetailsMono, VaDomain.class));
  }
}
