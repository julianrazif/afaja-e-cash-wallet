package com.julianraziffigaro.afajaecashwallet.web.controller;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractController {

  private final WebClient webClient;

  protected AbstractController(WebClient webClient) {
    this.webClient = webClient;
  }

  protected WebClient getWebClient() {
    return this.webClient;
  }
}
