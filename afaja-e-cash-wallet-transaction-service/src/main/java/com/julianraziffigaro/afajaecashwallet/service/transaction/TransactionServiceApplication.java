package com.julianraziffigaro.afajaecashwallet.service.transaction;

import com.julianraziffigaro.afajaecashwallet.service.transaction.kafka.KafkaRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@EnableDiscoveryClient
@SpringBootApplication(
  scanBasePackages = {
    "com.julianraziffigaro.afajaecashwallet.service.transaction",
    "com.julianraziffigaro.afajaecashwallet.core",
    "com.julianraziffigaro.afajaecashwallet.reactive.transaction",
    "com.julianraziffigaro.afajaecashwallet.kafka.transaction"
  }
)
public class TransactionServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactionServiceApplication.class, args);
  }

  @Bean
  public ApplicationRunner runner(KafkaRunner kafkaRunner) {
    return args -> {
      kafkaRunner.run();
      TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
    };
  }
}
