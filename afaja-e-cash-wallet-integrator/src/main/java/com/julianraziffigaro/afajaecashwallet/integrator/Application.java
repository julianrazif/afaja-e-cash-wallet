package com.julianraziffigaro.afajaecashwallet.integrator;

import com.julianraziffigaro.afajaecashwallet.core.kafka.processor.ReactorKafkaProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@EnableWebFlux
@SpringBootApplication(
  scanBasePackages = {
    "com.julianraziffigaro.afajaecashwallet.integrator",
    "com.julianraziffigaro.afajaecashwallet.core",
    "com.julianraziffigaro.afajaecashwallet.va",
    "com.julianraziffigaro.afajaecashwallet.reactive.va",
    "com.julianraziffigaro.afajaecashwallet.kafka.va"
  }
)
public class Application implements WebFluxConfigurer {

  private final ReactorKafkaProcessor kafkaReceiverTemplate;

  public Application(@Qualifier("vaReactorKafkaProcessorImpl") ReactorKafkaProcessor kafkaReceiverTemplate) {
    this.kafkaReceiverTemplate = kafkaReceiverTemplate;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ApplicationRunner runner() {
    return args -> kafkaReceiverTemplate.processor();
  }
}
