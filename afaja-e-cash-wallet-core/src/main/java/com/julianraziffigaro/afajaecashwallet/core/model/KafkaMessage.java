package com.julianraziffigaro.afajaecashwallet.core.model;

import com.julianraziffigaro.afajaecashwallet.core.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class KafkaMessage implements BaseDomain {

  private static final long serialVersionUID = -6417522398777222430L;

  private Long key;
  private String clientCode;
  private String method;
  private String messageId;
  private int ec;
  private Object data;
}
