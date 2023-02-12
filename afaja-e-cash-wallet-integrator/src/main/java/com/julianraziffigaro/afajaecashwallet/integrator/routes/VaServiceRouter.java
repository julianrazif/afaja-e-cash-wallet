package com.julianraziffigaro.afajaecashwallet.integrator.routes;

import com.julianraziffigaro.afajaecashwallet.integrator.handler.VaServiceHandler;
import com.julianraziffigaro.afajaecashwallet.integrator.handler.VaServiceReactiveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class VaServiceRouter {

  @Bean
  public RouterFunction<ServerResponse> vaServiceRoute(VaServiceHandler vaServiceHandler) {
    return RouterFunctions
      .route(RequestPredicates.POST("/vaUser/inquiry"), vaServiceHandler::inquiryVaUser);
  }

  @Bean
  public RouterFunction<ServerResponse> vaServiceReactiveRoute(VaServiceReactiveHandler vaServiceReactiveHandler) {
    return RouterFunctions
      .route(RequestPredicates.POST("/vaUserReactive/inquiry"), vaServiceReactiveHandler::inquiryVaUser);
  }
}
