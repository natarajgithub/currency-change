package com.nataraj.currencyExchange.service;

import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.builders.BillChangeResponseBuilder;
import com.nataraj.currencyExchange.entity.Bill;
import com.nataraj.currencyExchange.entity.BillChange;
import com.nataraj.currencyExchange.entity.Coin;
import com.nataraj.currencyExchange.exceptions.ChangeNotAvailableException;
import com.nataraj.currencyExchange.repository.BillChangeRepository;
import com.nataraj.currencyExchange.repository.BillRepository;
import com.nataraj.currencyExchange.repository.CoinRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillChangeService {

    private final BillRepository billRepository;
    private final CoinRepository coinRepository;
    private final BillChangeRepository billChangeRepository;

    @Transactional(dontRollbackOn=ChangeNotAvailableException.class)
    public List<BillChangeCoinResponseBean> getCoinChangeForBill(int amount, boolean maximumCoins){
        Bill bill = Bill.builder().billValue(new BigDecimal(amount)).build();
        billRepository.save(bill);
        List<Coin> availableCoins = coinRepository.findByCountGreaterThan(0);

        List<BillChange> changesForBill;
        try {
            changesForBill = processBillRequestForAvailableCoins(bill, availableCoins, maximumCoins);
        }finally{
            billRepository.updateStatus(bill.getId(), bill.getStatus());
            coinRepository.saveAllAndFlush(availableCoins);
        }

        return BillChangeResponseBuilder.getApiResponse(changesForBill);
    }


    private List<BillChange> processBillRequestForAvailableCoins(Bill bill, List<Coin> availableCoins, boolean maximumCoins) {
        List<BillChange> changesForBill;
        try {
            changesForBill = coinChange(availableCoins, bill, maximumCoins);
            billChangeRepository.saveAll(changesForBill);
            bill.setStatus(Bill.StatusType.SUCCESS);


        }catch(ChangeNotAvailableException e){
            bill.setStatus(Bill.StatusType.FAIL);
            throw e;
        }

        return changesForBill;
    }
    private List<BillChange> coinChange(List<Coin> coinList, Bill billAmount, boolean maxCoins){

        int amount = billAmount.getBillValue().intValue();
        Integer impossibleValue = (maxCoins)? Integer.MIN_VALUE: Integer.MAX_VALUE ;

        int[] coinsUsedForAmount = new int[amount + 1];
        int[][] coinsUsed = new int[amount + 1][coinList.size()];

        Arrays.fill(coinsUsedForAmount, impossibleValue);
        coinsUsedForAmount[0] = 0; // Base case: 0 coins required for amount 0

        for (int coinIndex = 0; coinIndex < coinList.size(); coinIndex++) {
            Coin coinAvailable = coinList.get(coinIndex);
            int coin = coinAvailable.getCoinValue().intValue();
            int count = Math.min(coinAvailable.getCount().intValue(), amount / coin);

            for (int currentAmount = amount; currentAmount >= coin; currentAmount--) {
                for (int coinCount = 1; coinCount <= count && coinCount * coin <= currentAmount; coinCount++) {
                    int remainingAmount = currentAmount - coinCount * coin;
                    boolean isAcceptableSolution;
                    if(maxCoins) {
                        isAcceptableSolution = coinsUsedForAmount[remainingAmount] != impossibleValue &&
                                coinsUsedForAmount[remainingAmount] + coinCount > coinsUsedForAmount[currentAmount];
                    }else {
                        isAcceptableSolution = coinsUsedForAmount[remainingAmount] != impossibleValue &&
                                coinsUsedForAmount[remainingAmount] + coinCount < coinsUsedForAmount[currentAmount];
                    }

                    if (isAcceptableSolution) {
                        coinsUsedForAmount[currentAmount] = coinsUsedForAmount[remainingAmount] + coinCount;
                        System.arraycopy(coinsUsed[remainingAmount], 0, coinsUsed[currentAmount], 0, coinList.size());
                        coinsUsed[currentAmount][coinIndex] = coinCount;
                    }

                }
            }
        }

        List<BillChange> billChangeList = new ArrayList<BillChange>();
        if (coinsUsedForAmount[amount] == impossibleValue) {
            throw new ChangeNotAvailableException("Required Change not available at the moment.");
        } else {

            int[] bestCaseCoinCombination = coinsUsed[amount];
            for (int i = 0; i < coinList.size(); i++) {
                if (bestCaseCoinCombination[i] > 0) {
                    Coin c = coinList.get(i);
                    c.setCount(c.getCount()-bestCaseCoinCombination[i]);

                    BillChange change = BillChange.builder()
                            .bill(billAmount)
                            .coin(c)
                            .count(Long.valueOf(bestCaseCoinCombination[i]))
                            .build();

                    billChangeList.add(change);
                }
            }
        }

        return billChangeList;
    }

}
