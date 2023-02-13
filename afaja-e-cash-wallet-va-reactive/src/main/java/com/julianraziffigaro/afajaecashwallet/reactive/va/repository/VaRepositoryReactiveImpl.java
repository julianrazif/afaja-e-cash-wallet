package com.julianraziffigaro.afajaecashwallet.reactive.va.repository;

import com.julianraziffigaro.afajaecashwallet.core.model.VaDetails;
import com.julianraziffigaro.afajaecashwallet.core.repository.VaRepositoryReactive;
import com.julianraziffigaro.afajaecashwallet.reactive.va.entity.VaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Repository
public class VaRepositoryReactiveImpl implements VaRepositoryReactive {

  private final R2dbcEntityTemplate r2dbcEntityTemplate;

  public VaRepositoryReactiveImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
    this.r2dbcEntityTemplate = r2dbcEntityTemplate;
  }

  @Override
  public Flux<VaDetails> findByVaNumber(String vaNumber) {
    return r2dbcEntityTemplate
      .select(VaEntity.class)
      .matching(Query.query(Criteria.where("va_number").is(vaNumber)).limit(1))
      .all()
      .doOnNext(vaEntity -> log.debug("vaEntity={}", vaEntity))
      .map(vaEntity -> VaEntity.entityToDetails(vaEntity).build());
  }

  @Override
  public Flux<VaDetails> findByParentVa(String parentVa) {
    return r2dbcEntityTemplate
      .select(VaEntity.class)
      .matching(Query.query(Criteria.where("parent_va").is(parentVa)).limit(50).offset(0))
      .all()
      .doOnNext(vaEntity -> log.debug("vaEntity={}", vaEntity))
      .map(vaEntity -> VaEntity.entityToDetails(vaEntity).build());
  }

  @Override
  public Flux<VaDetails> findByPhoneNumber(String phoneNumber) {
    return r2dbcEntityTemplate
      .select(VaEntity.class)
      .matching(Query.query(Criteria.where("phone_number").is(phoneNumber)).limit(1))
      .all()
      .doOnNext(vaEntity -> log.debug("vaEntity={}", vaEntity))
      .map(vaEntity -> VaEntity.entityToDetails(vaEntity).build());
  }

  @Override
  public Mono<VaDetails> save(String vaNumber, String parentVa, String realName, String phoneNumber, BigDecimal currentBalance, String hashedCode) {
    Mono<String> vaNumberMono = r2dbcEntityTemplate
      .getDatabaseClient()
      .sql(""
        + "INSERT INTO va (va_number, parent_va, real_name, phone_number, current_balance, hashed_code) "
        + "VALUES (:vaNumber, :parentVa, :realName, :phoneNumber, :currentBalance, :hashedCode)"
        + ""
      )
      .filter((statement, next) -> statement.returnGeneratedValues("va_number").execute())
      .bind("vaNumber", vaNumber)
      .bind("parentVa", parentVa)
      .bind("realName", realName)
      .bind("phoneNumber", phoneNumber)
      .bind("currentBalance", currentBalance)
      .bind("hashedCode", hashedCode)
      .fetch()
      .first()
      .map(stringObjectMap -> stringObjectMap.get("va_number").toString());

     return vaNumberMono.flatMap(s -> findByVaNumber(s).singleOrEmpty());
  }
}
