package com.julianraziffigaro.afajaecashwallet.kafka.transaction.config;

import com.julianraziffigaro.afajaecashwallet.core.config.KafkaConsumerConfigData;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@RequiredArgsConstructor
public class OSHostName {

  private final KafkaConsumerConfigData kafkaConsumerConfigData;

  public String osHostname() {
    String hostname = "-unknown";
    try {
      hostname = "-" + InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      ex.printStackTrace();
    }
    hostname += "-" + (new Random().nextInt(5 * 3 - 1 + 1) + 1)
      + "-" + (new Random().nextInt(15 - 1 + 1) + 1);
    return hostname;
  }

  public String clientIdWrapper() {
    return kafkaConsumerConfigData.getClientId() + osHostname();
  }
}
