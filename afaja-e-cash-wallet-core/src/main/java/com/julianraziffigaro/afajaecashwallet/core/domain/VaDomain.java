package com.julianraziffigaro.afajaecashwallet.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VaDomain implements BaseDomain {

  private static final long serialVersionUID = -179880855010858629L;

  private String vaNumber;
  private String parentVa;
  private String realName;
  private String phoneNumber;
  private BigDecimal currentBalance;
  private String hashedCode;
}
