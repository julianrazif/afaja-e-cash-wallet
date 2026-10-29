package com.julianraziffigaro.afajaecashwallet.core.domain;

import com.julianraziffigaro.afajaecashwallet.core.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDomain implements BaseDomain {

  private static final long serialVersionUID = -5664990705159957491L;

  private Long trxId;
  private VaDomain issuedBy;
  private String issuedDate;
  private TransactionType transactionType;
  private BigDecimal amount;
  private String hashedCode;
}
