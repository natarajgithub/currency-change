package com.nataraj.currencyExchange.service;

import com.nataraj.currencyExchange.bean.CoinResponseBean;
import com.nataraj.currencyExchange.repository.CoinRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CoinService {

    private final CoinRepository coinRepository;

    public void setCoinsCount(String coinValue, Integer countCount){
        coinRepository.updateCoinCount(new BigDecimal(coinValue), Long.valueOf(countCount));
    }

    public List<CoinResponseBean> getCoinsStatus() {
        return coinRepository.findAll().stream().map(coin -> CoinResponseBean.builder()
                .coinValue(coin.getCoinValue().intValue())
                .coinCount(Math.toIntExact(coin.getCount()))
                .build()).toList();
    }

}
