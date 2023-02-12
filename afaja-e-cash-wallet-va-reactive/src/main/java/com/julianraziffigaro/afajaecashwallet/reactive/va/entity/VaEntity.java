package com.julianraziffigaro.afajaecashwallet.reactive.va.entity;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Table(name = "va", schema = "dev")
public class VaEntity implements VaDetails {

  private static final long serialVersionUID = -5466890638901801055L;

  @Id
  private final String vaNumber;
  private final String parentVa;
  private final String realName;
  private final String phoneNumber;
  private final BigDecimal currentBalance;
  private final String hashedCode;

  public static VaEntityBuilder builder() {
    return new VaEntityBuilder();
  }

  public static VaEntityBuilder entityToDetails(VaDetails vaDetails) {
    return builder()
      .vaNumber(vaDetails.getVaNumber())
      .parentVa(vaDetails.getParentVa())
      .realName(vaDetails.getRealName())
      .phoneNumber(vaDetails.getPhoneNumber())
      .withCurrentBalance(vaDetails.getCurrentBalance())
      .hashedCode(vaDetails.getHashedCode());
  }

  public static final class VaEntityBuilder {

    private String vaNumber;
    private String parentVa;
    private String realName;
    private String phoneNumber;
    private BigDecimal currentBalance;
    private String hashedCode;

    private VaEntityBuilder() {
    }

    public VaEntityBuilder vaNumber(String vaNumber) {
      this.vaNumber = vaNumber;
      return this;
    }

    public VaEntityBuilder parentVa(String parentVa) {
      this.parentVa = parentVa;
      return this;
    }

    public VaEntityBuilder realName(String realName) {
      this.realName = realName;
      return this;
    }

    public VaEntityBuilder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public VaEntityBuilder withCredit(BigDecimal amount) {
      this.currentBalance = this.currentBalance.add(amount);
      return this;
    }

    public VaEntityBuilder withDebit(BigDecimal amount) {
      this.currentBalance = this.currentBalance.subtract(amount);
      return this;
    }

    public VaEntityBuilder withCurrentBalance(BigDecimal currentBalance) {
      this.currentBalance = currentBalance;
      return this;
    }

    public VaEntityBuilder hashedCode(String hashedCode) {
      this.hashedCode = hashedCode;
      return this;
    }

    public VaDetails build() {
      return new VaEntity(this.vaNumber, this.parentVa, this.realName, this.phoneNumber, this.currentBalance, this.hashedCode);
    }
  }
}
