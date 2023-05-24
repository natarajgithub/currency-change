package com.nataraj.currencyExchange.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;

@Data
public class CoinRequestBean {

    @NonNull
    @Pattern(regexp="^(1|5|10|25)$", message="invalid Value")
    private String coinValue;

    @NonNull @Min(0)
    private Integer coinCount;

}
