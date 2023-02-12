package com.julianraziffigaro.afajaecashwallet.integrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@EnableWebFlux
@SpringBootApplication(
  scanBasePackages = {
    "com.julianraziffigaro.afajaecashwallet.integrator",
    "com.julianraziffigaro.afajaecashwallet.core",
    "com.julianraziffigaro.afajaecashwallet.va",
    "com.julianraziffigaro.afajaecashwallet.reactive.va"
  },
  exclude = {
    JmxAutoConfiguration.class
  }
)
public class Application implements WebFluxConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
