package com.julianraziffigaro.afajaecashwallet.web.controller;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.dto.request.RequestDTO;
import com.julianraziffigaro.afajaecashwallet.core.dto.response.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = {"/vaUserReactive"})
public class VaUserReactiveController extends AbstractController {

  private final WebClient webClient;

  public VaUserReactiveController(WebClient webClient) {
    super(webClient);
    this.webClient = webClient;
  }

  @PostMapping(value = {"/inquiry"})
  public Mono<ResponseDTO<VaDomain>> inquiryVaUser(@RequestBody RequestDTO<VaDomain> requestDTO) {
    VaDomain vaDomain = requestDTO.getData();

    return this.webClient
      .post()
      .uri("/vaUserReactive/inquiry")
      .body(Mono.just(vaDomain), VaDomain.class)
      .retrieve()
      .bodyToMono(VaDomain.class)
      .map(ResponseDTO::new)
      .log();
  }
}
