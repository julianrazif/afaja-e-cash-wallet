package com.julianraziffigaro.afajaecashwallet.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Transaction implements TransactionDetails {

  private static final long serialVersionUID = -6638711120843590499L;

  private final Long trxId;
  private final Va issuedBy;
  private final Date issuedDate;
  private final TransactionType transactionType;
  private final BigDecimal amount;
  private final String hashedCode;

  public static TransactionBuilder builder() {
    return new TransactionBuilder();
  }

  public static TransactionBuilder withTransactionDetails(TransactionDetails transactionDetails) {
    return builder()
      .trxId(transactionDetails.getTrxId())
      .issuedBy(transactionDetails.getIssuedBy())
      .issuedDate(transactionDetails.getIssuedDate())
      .transactionType(transactionDetails.getTransactionType())
      .amount(transactionDetails.getAmount())
      .hashedCode(transactionDetails.getHashedCode());
  }

  public static final class TransactionBuilder {

    private Long trxId;
    private Va issuedBy;
    private Date issuedDate;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String hashedCode;

    private TransactionBuilder() {
    }

    public TransactionBuilder trxId(Long trxId) {
      this.trxId = trxId;
      return this;
    }

    public TransactionBuilder issuedBy(Va issuedBy) {
      this.issuedBy = issuedBy;
      return this;
    }

    public TransactionBuilder issuedDate(Date issuedDate) {
      this.issuedDate = issuedDate;
      return this;
    }

    public TransactionBuilder transactionType(TransactionType transactionType) {
      this.transactionType = transactionType;
      return this;
    }

    public TransactionBuilder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    public TransactionBuilder hashedCode(String hashedCode) {
      this.hashedCode = hashedCode;
      return this;
    }

    public TransactionDetails build() {
      return new Transaction(this.trxId, this.issuedBy, this.issuedDate, this.transactionType, this.amount, this.hashedCode);
    }
  }
}
