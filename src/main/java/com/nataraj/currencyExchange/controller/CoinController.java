package com.nataraj.currencyExchange.controller;


import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.bean.CoinRequestBean;
import com.nataraj.currencyExchange.bean.CoinResponseBean;
import com.nataraj.currencyExchange.entity.Coin;
import com.nataraj.currencyExchange.service.CoinService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Validated
public class CoinController {

    private final CoinService coinService;

    @PostMapping(value = "/coin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void setCoinCount(@Valid @RequestBody CoinRequestBean coinRequestBean){
        coinService.setCoinsCount(coinRequestBean.getCoinValue(), coinRequestBean.getCoinCount());
    }

    @GetMapping(value = "/coin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CoinResponseBean> setCoinCount(){
        return coinService.getCoinsStatus();
    }
}
