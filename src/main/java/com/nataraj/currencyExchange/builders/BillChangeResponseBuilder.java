package com.nataraj.currencyExchange.builders;

import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.entity.BillChange;

import java.util.List;

public class BillChangeResponseBuilder {

    public static List<BillChangeCoinResponseBean> getApiResponse(List<BillChange> changesForBill){
        return changesForBill.stream().map(BillChangeResponseBuilder::mapToChangeCoinResponse).toList();
    }

    private static BillChangeCoinResponseBean mapToChangeCoinResponse(BillChange billChange){
        return BillChangeCoinResponseBean.builder()
                .coinValue(billChange.getCoin().getCoinValue().intValue())
                .coinCount(billChange.getCount().intValue())
                .build();
    }
}
