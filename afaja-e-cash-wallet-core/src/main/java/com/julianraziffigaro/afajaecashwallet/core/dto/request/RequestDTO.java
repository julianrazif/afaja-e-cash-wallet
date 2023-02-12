package com.julianraziffigaro.afajaecashwallet.core.dto.request;

import com.julianraziffigaro.afajaecashwallet.core.domain.BaseDomain;
import com.julianraziffigaro.afajaecashwallet.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestDTO<T extends BaseDomain> implements BaseDTO {

  private static final long serialVersionUID = 9183537423080682080L;

  private T data;
}
