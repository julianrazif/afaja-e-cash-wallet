package com.julianraziffigaro.afajaecashwallet.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public interface VaDetails extends Serializable {

  String getVaNumber();

  String getParentVa();

  String getRealName();

  String getPhoneNumber();

  BigDecimal getCurrentBalance();

  String getHashedCode();
}
