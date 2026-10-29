package com.julianraziffigaro.afajaecashwallet.reactive.transaction.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.Transaction;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionType;
import com.julianraziffigaro.afajaecashwallet.core.model.Va;
import com.julianraziffigaro.afajaecashwallet.core.repository.TransactionRepositoryReactive;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public class TransactionRepositoryReactiveImpl implements TransactionRepositoryReactive {

  private final R2dbcEntityTemplate r2dbcEntityTemplate;

  public TransactionRepositoryReactiveImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
    this.r2dbcEntityTemplate = r2dbcEntityTemplate;
  }

  @Override
  public Mono<String> save(Long trxId, String issuedBy, Date issuedDate,
                           String transactionType, BigDecimal amount, String hashedCode) {

    return r2dbcEntityTemplate
      .getDatabaseClient()
      .sql(""
        + "INSERT INTO transaction (issued_by, issued_date, transaction_type, amount, hashed_code) "
        + "VALUES (:issuedBy,:issuedDate, :transactionType, :amount, :hashedCode)"
        + ""
      )
      .filter((statement, next) -> statement.returnGeneratedValues("trx_id").execute())
      .bind("issuedBy", issuedBy)
      .bind("issuedDate", issuedDate)
      .bind("transactionType", transactionType)
      .bind("amount", amount)
      .bind("hashedCode", hashedCode)
      .fetch()
      .first()
      .map(stringObjectMap -> stringObjectMap.get("trx_id").toString());
  }

  @Override
  public Flux<TransactionDetails> findByTrxId(Long trxId) {
    return r2dbcEntityTemplate
      .getDatabaseClient()
      .sql(""
        + "select t.*, v.* from \"transaction\" t inner join va v on t.issued_by = v.va_number "
        + "where t.trx_id = :trxId limit 1"
        + ""
      )
      .bind("trxId", trxId)
      .map((row, rowMetadata) -> {
        Transaction.TransactionBuilder transactionBuilder = Transaction
          .builder()
          .trxId(row.get("trx_id", Long.class))
          .issuedDate(row.get("issued_date", Date.class))
          .transactionType(TransactionType.valueOf(row.get("transaction_type", String.class)))
          .amount(row.get("amount", BigDecimal.class))
          .hashedCode(row.get("hashed_code", String.class));

        Va va = new Va(
          row.get("va_number", String.class),
          row.get("parent_va", String.class),
          row.get("real_name", String.class),
          row.get("phone_number", String.class),
          row.get("current_balance", BigDecimal.class),
          row.get("hashed_code", String.class)
        );

        return transactionBuilder.issuedBy(va).build();
      })
      .all();
  }
}
