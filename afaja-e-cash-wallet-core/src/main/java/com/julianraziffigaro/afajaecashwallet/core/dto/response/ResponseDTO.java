package com.julianraziffigaro.afajaecashwallet.core.dto.response;

import com.julianraziffigaro.afajaecashwallet.core.domain.BaseDomain;
import com.julianraziffigaro.afajaecashwallet.core.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T extends BaseDomain> implements BaseDTO {

  private static final long serialVersionUID = -4109229722339758382L;

  private T data;
}
