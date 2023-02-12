package com.julianraziffigaro.afajaecashwallet.core.dto;

import com.julianraziffigaro.afajaecashwallet.core.domain.BaseDomain;

import java.io.Serializable;

public interface BaseDTO extends Serializable {

  BaseDomain getData();
}
