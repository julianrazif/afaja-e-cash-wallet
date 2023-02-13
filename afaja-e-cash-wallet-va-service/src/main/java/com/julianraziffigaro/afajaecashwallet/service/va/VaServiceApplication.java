package com.julianraziffigaro.afajaecashwallet.service.va;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(
  scanBasePackages = {
    "com.julianraziffigaro.afajaecashwallet.service.va",
    "com.julianraziffigaro.afajaecashwallet.reactive.va"
  },
  exclude = {
    JmxAutoConfiguration.class
  }
)
public class VaServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(VaServiceApplication.class, args);
  }
}
