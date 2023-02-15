package com.julianraziffigaro.afajaecashwallet.transaction.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.Transaction;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionDetails;
import com.julianraziffigaro.afajaecashwallet.core.model.TransactionType;
import com.julianraziffigaro.afajaecashwallet.core.model.Va;
import com.julianraziffigaro.afajaecashwallet.core.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

  private final JdbcTemplate jdbcTemplate;

  public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<String> save(Long trxId, String issuedBy, Date issuedDate, String transactionType,
                               BigDecimal amount, String hashedCode) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(""
          + "INSERT INTO transaction (issued_by, issued_date, transaction_type, amount, hashed_code) "
          + "VALUES (?, ?, ?, ?, ?)"
          + "",
        Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, issuedBy);
      ps.setDate(2, new java.sql.Date(issuedDate.getTime()));
      ps.setString(3, transactionType);
      ps.setBigDecimal(4, amount);
      ps.setString(5, hashedCode);
      return ps;
    }, keyHolder);

    return Optional.of(keyHolder.getKeyList().get(0).get("trx_id").toString());
  }

  @Override
  public Stream<TransactionDetails> findByTrxId(Long trxId) {
    return jdbcTemplate.queryForStream(con -> {
      PreparedStatement ps = con.prepareStatement(""
        + "select t.*, v.* from \"transaction\" t inner join va v on t.issued_by = v.va_number "
        + "where t.trx_id = ? limit 1"
        + "");
      ps.setLong(1, trxId);
      return ps;
    }, (rs, rowNum) -> {
      Transaction.TransactionBuilder transactionBuilder = Transaction
        .builder()
        .trxId(rs.getLong(1))
        .issuedDate(rs.getDate(3))
        .transactionType(TransactionType.valueOf(rs.getString(4)))
        .amount(rs.getBigDecimal(5))
        .hashedCode(rs.getString(6));

      Va va = new Va(
        rs.getString(7),
        rs.getString(8),
        rs.getString(9),
        rs.getString(10),
        rs.getBigDecimal(11),
        rs.getString(12)
      );

      return transactionBuilder.issuedBy(va).build();
    });
  }
}
