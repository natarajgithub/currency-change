package com.nataraj.currencyExchange.controller;

import com.nataraj.currencyExchange.bean.BillChangeCoinResponseBean;
import com.nataraj.currencyExchange.service.BillChangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest
class ExchangeControllerTest {

    @MockBean
    private BillChangeService billChangeService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetChangeForBill_Success() throws Exception {
        // Mocking data
        String billAmount = "10";
        boolean maximumCoins = false;
        List<BillChangeCoinResponseBean> expectedResponse = Arrays.asList(
                new BillChangeCoinResponseBean(1, 5),
                new BillChangeCoinResponseBean(2, 2)
        );
        // Configure mock service
        when(billChangeService.getCoinChangeForBill(1000, maximumCoins)).thenReturn(expectedResponse);
        // Perform the test
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/change")
                        .param("bill", billAmount))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"coinValue\":1,\"coinCount\":5},{\"coinValue\":2,\"coinCount\":2}]"))
                .andReturn();
    }

    @Test
    void testGetChangeForBill_InvalidBill() throws Exception {
        // Mocking data
        String billAmount = "2121";
        // Perform the test
        mockMvc.perform(MockMvcRequestBuilders.get("/change")
                        .param("bill", billAmount).param("maximumCoins", "false"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                ;
    }

}
