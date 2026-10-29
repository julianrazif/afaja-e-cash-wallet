package com.julianraziffigaro.afajaecashwallet.service.va;

import com.julianraziffigaro.afajaecashwallet.service.va.kafka.KafkaRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication(
  scanBasePackages = {
    "com.julianraziffigaro.afajaecashwallet.service.va",
    "com.julianraziffigaro.afajaecashwallet.core",
    "com.julianraziffigaro.afajaecashwallet.reactive.va",
    "com.julianraziffigaro.afajaecashwallet.kafka.va"
  }
)
public class VaServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(VaServiceApplication.class, args);
  }

  @Bean
  public ApplicationRunner runner(KafkaRunner kafkaRunner) {
    return args -> {
      kafkaRunner.run();
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
    };
  }
}
