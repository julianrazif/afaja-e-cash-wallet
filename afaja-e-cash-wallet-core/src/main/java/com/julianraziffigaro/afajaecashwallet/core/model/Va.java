package com.julianraziffigaro.afajaecashwallet.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Va implements VaDetails {

  private static final long serialVersionUID = -4298717054849578802L;

  private final String vaNumber;
  private final String parentVa;
  private final String realName;
  private final String phoneNumber;
  private final BigDecimal currentBalance;
  private final String hashedCode;

  public static VaBuilder builder() {
    return new VaBuilder();
  }

  public static VaBuilder withVaDetails(VaDetails vaDetails) {
    return builder()
      .vaNumber(vaDetails.getVaNumber())
      .parentVa(vaDetails.getParentVa())
      .realName(vaDetails.getRealName())
      .phoneNumber(vaDetails.getPhoneNumber())
      .withCurrentBalance(vaDetails.getCurrentBalance())
      .hashedCode(vaDetails.getHashedCode());
  }

  public static final class VaBuilder {

    private String vaNumber;
    private String parentVa;
    private String realName;
    private String phoneNumber;
    private BigDecimal currentBalance;
    private String hashedCode;

    private VaBuilder() {
    }

    public VaBuilder vaNumber(String vaNumber) {
      this.vaNumber = vaNumber;
      return this;
    }

    public VaBuilder parentVa(String parentVa) {
      this.parentVa = parentVa;
      return this;
    }

    public VaBuilder realName(String realName) {
      this.realName = realName;
      return this;
    }

    public VaBuilder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public VaBuilder withCredit(BigDecimal amount) {
      this.currentBalance = this.currentBalance.add(amount);
      return this;
    }

    public VaBuilder withDebit(BigDecimal amount) {
      this.currentBalance = this.currentBalance.subtract(amount);
      return this;
    }

    public VaBuilder withCurrentBalance(BigDecimal currentBalance) {
      this.currentBalance = currentBalance;
      return this;
    }

    public VaBuilder hashedCode(String hashedCode) {
      this.hashedCode = hashedCode;
      return this;
    }

    public VaDetails build() {
      return new Va(this.vaNumber, this.parentVa, this.realName, this.phoneNumber, this.currentBalance, this.hashedCode);
    }
  }
}
