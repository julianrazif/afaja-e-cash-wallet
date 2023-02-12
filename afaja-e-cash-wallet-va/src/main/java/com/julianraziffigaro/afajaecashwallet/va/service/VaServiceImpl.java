package com.julianraziffigaro.afajaecashwallet.va.service;

import com.julianraziffigaro.afajaecashwallet.core.domain.VaDomain;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.service.VaService;
import com.julianraziffigaro.afajaecashwallet.va.repository.VaRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VaServiceImpl implements VaService {

  private final VaRepositoryImpl vaRepository;

  public VaServiceImpl(VaRepositoryImpl vaRepository) {
    this.vaRepository = vaRepository;
  }

  @Override
  public Optional<VaDetails> save(VaDomain vaDomain) {
    return this.vaRepository.save(
      vaDomain.getVaNumber(),
      vaDomain.getParentVa(),
      vaDomain.getRealName(),
      vaDomain.getPhoneNumber(),
      vaDomain.getCurrentBalance(),
      vaDomain.getHashedCode()
    ).findFirst();
  }

  @Override
  public Optional<VaDetails> inquiry(VaDomain vaDomain) {
    String vaNumber = vaDomain.getVaNumber();
    String phoneNumber = vaDomain.getPhoneNumber();

    if (vaNumber != null) {
      return this.vaRepository.findByVaNumber(vaNumber).findFirst();
    } else if (phoneNumber != null) {
      return this.vaRepository.findByPhoneNumber(phoneNumber).findFirst();
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Stream<VaDetails> allByParent(VaDomain vaDomain) {
    String parentVa = vaDomain.getParentVa();

    if (parentVa != null) {
      return this.vaRepository.findByParentVa(parentVa);
    } else {
      return Stream.empty();
    }
  }
}
