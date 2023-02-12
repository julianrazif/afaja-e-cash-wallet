package com.julianraziffigaro.afajaecashwallet.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public interface TransactionDetails extends Serializable {

  Long getTrxId();

  Va getIssuedBy();

  Date getIssuedDate();

  TransactionType getTransactionType();

  BigDecimal getAmount();

  String getHashedCode();
}
