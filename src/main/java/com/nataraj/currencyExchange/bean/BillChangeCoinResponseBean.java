package com.nataraj.currencyExchange.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillChangeCoinResponseBean {

    private Integer coinValue;
    private Integer coinCount;

}
