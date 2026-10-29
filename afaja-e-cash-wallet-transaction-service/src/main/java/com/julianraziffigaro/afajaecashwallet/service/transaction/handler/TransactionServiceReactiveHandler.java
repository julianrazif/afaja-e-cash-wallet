package com.julianraziffigaro.afajaecashwallet.service.transaction.handler;

import com.julianraziffigaro.afajaecashwallet.core.domain.TransactionDomain;
import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.service.transaction.service.TransactionServiceIntegratorImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.ParseException;

@Component
public class TransactionServiceReactiveHandler {

  private final TransactionServiceIntegratorImpl transactionServiceIntegrator;

  public TransactionServiceReactiveHandler(TransactionServiceIntegratorImpl transactionServiceIntegrator) {
    this.transactionServiceIntegrator = transactionServiceIntegrator;
  }

  public Mono<ServerResponse> submitTransaction(ServerRequest request) {
    Mono<VaDomain> reqReply = request
      .bodyToMono(TransactionDomain.class)
      .flatMap(transactionDomain -> {
        try {
          return transactionServiceIntegrator.submitTransaction(transactionDomain).singleOrEmpty();
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }).log();

    return ServerResponse
      .ok()
      .body(BodyInserters.fromPublisher(reqReply, VaDomain.class));
  }
}
