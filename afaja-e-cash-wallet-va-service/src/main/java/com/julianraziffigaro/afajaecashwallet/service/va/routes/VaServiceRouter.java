package com.julianraziffigaro.afajaecashwallet.service.va.routes;

import com.julianraziffigaro.afajaecashwallet.service.va.handler.VaServiceReactiveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class VaServiceRouter {

  @Bean
  public RouterFunction<ServerResponse> vaServiceReactiveRoute(VaServiceReactiveHandler vaServiceReactiveHandler) {
    return RouterFunctions
      .route(RequestPredicates.POST("/vaUserReactive/inquiry"), vaServiceReactiveHandler::inquiryVaUser)
      .andRoute(RequestPredicates.POST("/vaUserReactive/byParent"), vaServiceReactiveHandler::allVaUserByParent)
      .andRoute(RequestPredicates.POST("/vaUserReactive/createVaUser"), vaServiceReactiveHandler::createVaUser);
  }
}
