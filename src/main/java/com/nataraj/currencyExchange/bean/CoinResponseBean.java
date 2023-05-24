package com.nataraj.currencyExchange.bean;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinResponseBean {
    private Integer coinValue;
    private Integer coinCount;
}
