package com.nataraj.currencyExchange.repository;


import com.nataraj.currencyExchange.entity.Bill;
import com.nataraj.currencyExchange.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findByCountGreaterThan(int coinCount);

    @Modifying(clearAutomatically = true)
    @Query("update Coin coin set coin.count =:coinCount where coin.coinValue =:coinValue")
    void updateCoinCount(@Param("coinValue") BigDecimal coinValue, @Param("coinCount") Long count);
}