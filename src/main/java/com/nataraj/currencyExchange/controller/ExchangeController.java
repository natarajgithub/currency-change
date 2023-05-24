package com.nataraj.currencyExchange.controller;


import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.service.BillChangeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ExchangeController {

    private final BillChangeService billChangeService;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public String ping(){
        return new Date()+"";
    }

    @GetMapping(value = "/change", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BillChangeCoinResponseBean> getChangeForBill(@Valid @RequestParam("bill")
                                                                 @Pattern(regexp="^(1|2|5|10|20|50|100)$", message="invalid Bill")
                                                                 String billAmount,
                                                             @RequestParam(value = "minimumCoins", required = false, defaultValue = "false") boolean maximumCoins){
        return billChangeService.getCoinChangeForBill((Integer.parseInt(billAmount)*100), maximumCoins);
    }

}
