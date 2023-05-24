package com.nataraj.currencyExchange.service;

import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.entity.Bill;
import com.nataraj.currencyExchange.entity.BillChange;
import com.nataraj.currencyExchange.entity.Coin;
import com.nataraj.currencyExchange.exceptions.ChangeNotAvailableException;
import com.nataraj.currencyExchange.repository.BillChangeRepository;
import com.nataraj.currencyExchange.repository.BillRepository;
import com.nataraj.currencyExchange.repository.CoinRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class BillChangeServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private CoinRepository coinRepository;
    @Mock
    private BillChangeRepository billChangeRepository;

    private BillChangeService billChangeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        billChangeService = new BillChangeService(billRepository, coinRepository, billChangeRepository);
    }

    @Test
    void testGetMaxCoinChangeForBill_Success() {
        // Mocking data
        int amount = 100;
        boolean maximumCoins = true;
        List<Coin> availableCoins = Arrays.asList(
                new Coin(1L, "", new BigDecimal(1), 100L, null, null, null),
                new Coin(1L, "", new BigDecimal(5), 100L, null, null, null),
                new Coin(1L, "", new BigDecimal(10), 100L, null, null, null),
                new Coin(2L, "", new BigDecimal(25), 100L, null, null, null)
        );
        Bill bill = new Bill(1L, "", new BigDecimal(amount), null, null, null);
        List<BillChange> expectedChanges = Arrays.asList(
                new BillChange(1L, bill, availableCoins.get(0), 5L, null),
                new BillChange(2L, bill, availableCoins.get(1), 2L, null)
        );
        List<BillChangeCoinResponseBean> expectedResponse = new ArrayList<>();
        expectedResponse.add(BillChangeCoinResponseBean.builder().coinValue(1).coinCount(100).build());
        // Configure mock repositories
        when(coinRepository.findByCountGreaterThan(0)).thenReturn(availableCoins);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        when(billChangeRepository.saveAll(anyList())).thenReturn(expectedChanges);
        // Perform the test
        List<BillChangeCoinResponseBean> actualResponse = billChangeService.getCoinChangeForBill(amount, maximumCoins);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetMinCoinChangeForBill_Success() {
        // Mocking data
        int amount = 100;
        boolean maximumCoins = false;
        List<Coin> availableCoins = Arrays.asList(
                new Coin(1L, "", new BigDecimal(1), 100L, null, null, null),
                new Coin(1L, "", new BigDecimal(5), 100L, null, null, null),
                new Coin(1L, "", new BigDecimal(10), 100L, null, null, null),
                new Coin(2L, "", new BigDecimal(25), 100L, null, null, null)
        );
        Bill bill = new Bill(1L, "", new BigDecimal(amount), null, null, null);
        List<BillChange> expectedChanges = Arrays.asList(
                new BillChange(1L, bill, availableCoins.get(0), 5L, null),
                new BillChange(2L, bill, availableCoins.get(1), 2L, null)
        );
        List<BillChangeCoinResponseBean> expectedResponse = new ArrayList<>();
        expectedResponse.add(BillChangeCoinResponseBean.builder().coinValue(25).coinCount(4).build());
        // Configure mock repositories
        when(coinRepository.findByCountGreaterThan(0)).thenReturn(availableCoins);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        when(billChangeRepository.saveAll(anyList())).thenReturn(expectedChanges);
        // Perform the test
        List<BillChangeCoinResponseBean> actualResponse = billChangeService.getCoinChangeForBill(amount, maximumCoins);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetCoinChangeForBill_Failure() {
        // Mocking data
        int amount = 100;
        boolean maximumCoins = true;
        List<Coin> availableCoins = Arrays.asList(
                new Coin(1L, "", new BigDecimal(1), 0L,null, null, null),
                new Coin(2L, "", new BigDecimal(5), 5L, null, null, null)
        );
        Bill bill = new Bill(1L, "", new BigDecimal(amount), null, null, null);
        // Configure mock repositories
        when(coinRepository.findByCountGreaterThan(0)).thenReturn(availableCoins);
        when(billRepository.save(any(Bill.class))).thenReturn(bill);
        doThrow(new ChangeNotAvailableException("Required Change not available at the moment."))
                .when(billChangeRepository).saveAll(anyList());
        // Perform the test
        Assertions.assertThrows(ChangeNotAvailableException.class, () ->
                billChangeService.getCoinChangeForBill(amount, maximumCoins)
        );

    }

    // Add more unit tests for different scenarios

}
