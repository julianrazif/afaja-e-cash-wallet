package com.julianraziffigaro.afajaecashwallet.service.transaction.routes;

import com.julianraziffigaro.afajaecashwallet.service.transaction.handler.TransactionServiceReactiveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransactionServiceRouter {

  @Bean
  public RouterFunction<ServerResponse> vaServiceReactiveRoute(TransactionServiceReactiveHandler transactionServiceReactiveHandler) {
    return RouterFunctions
      .route(RequestPredicates.POST("/transactionReactive/submitTransaction"), transactionServiceReactiveHandler::submitTransaction);
  }
}
