package com.julianraziffigaro.afajaecashwallet.va.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.Va;
import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.repository.VaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class VaRepositoryImpl implements VaRepository {

  private final JdbcTemplate jdbcTemplate;

  public VaRepositoryImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Stream<VaDetails> findByVaNumber(String vaNumber) {
    return jdbcTemplate.queryForStream(con -> {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM va WHERE va_number = ? limit 1");
      ps.setString(1, vaNumber);
      return ps;
    }, (rs, rowNum) -> Va.withVaDetails(
        new Va(
          rs.getString(1),
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          rs.getBigDecimal(5),
          rs.getString(6)
        )
      )
      .build());
  }

  @Override
  public Stream<VaDetails> findByParentVa(String parentVa) {
    return jdbcTemplate.queryForStream(con -> {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM va WHERE parent_va = ? limit 50");
      ps.setString(1, parentVa);
      return ps;
    }, (rs, rowNum) -> Va.withVaDetails(
        new Va(
          rs.getString(1),
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          rs.getBigDecimal(5),
          rs.getString(6)
        )
      )
      .build());
  }

  @Override
  public Stream<VaDetails> findByPhoneNumber(String phoneNumber) {
    return jdbcTemplate.queryForStream(con -> {
      PreparedStatement ps = con.prepareStatement("SELECT * FROM va WHERE phone_number = ? limit 1");
      ps.setString(1, phoneNumber);
      return ps;
    }, (rs, rowNum) -> Va.withVaDetails(
        new Va(
          rs.getString(1),
          rs.getString(2),
          rs.getString(3),
          rs.getString(4),
          rs.getBigDecimal(5),
          rs.getString(6)
        )
      )
      .build());
  }

  @Override
  public Optional<String> save(String vaNumber, String parentVa, String realName, String phoneNumber,
                       BigDecimal currentBalance, String hashedCode) {

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(""
          + "INSERT INTO va (va_number, parent_va, real_name, phone_number, current_balance, hashed_code) "
          + "VALUES (?, ?, ?, ?, ?, ?)"
          + "",
        Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, vaNumber);
      ps.setString(2, parentVa);
      ps.setString(3, realName);
      ps.setString(4, phoneNumber);
      ps.setBigDecimal(5, currentBalance);
      ps.setString(6, hashedCode);
      return ps;
    }, keyHolder);

    return Optional.of(keyHolder.getKeyList().get(0).get("va_number").toString());
  }

  @Override
  public Optional<String> debitCredit(String vaNumber, BigDecimal amount) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(""
          + "UPDATE va SET current_balance = ? WHERE va_number = ?"
          + "",
        Statement.RETURN_GENERATED_KEYS
      );
      ps.setBigDecimal(1, amount);
      ps.setString(2, vaNumber);
      return ps;
    }, keyHolder);

    return Optional.of(keyHolder.getKeyList().get(0).get("va_number").toString());
  }
}
