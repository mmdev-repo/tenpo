package com.tenpo.contoller;

import com.tenpo.interceptor.RateLimitInterceptor;
import com.tenpo.model.HistoryAverage;
import com.tenpo.repositoryredis.RedisRepository;
import com.tenpo.service.TenpoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest
public class TenpoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenpoServiceImpl tenpoServiceImpl;

    @MockBean
    private RedisRepository redisRepository;

    @MockBean
    private RateLimitInterceptor rateLimitInterceptor;

    @Test
    void getDinamicAverageReturnsCorrectValue() throws Exception {

        when(tenpoServiceImpl.getDinamicAverage(Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocation -> 5.0);
        when(rateLimitInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(true);

        mockMvc.perform(get("/v1/dinamic-average")
                        .param("num1", "4")
                        .param("num2", "6"))
                .andExpect(status().isOk())
                .andExpect(content().string("5.0"));
    }

    @Test
    void getDinamicAverageWithNegativeNumbersReturnsBadRequest() throws Exception {
        when(rateLimitInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(true);

        mockMvc.perform(get("/v1/dinamic-average")
                        .param("num1", "-4")
                        .param("num2", "6"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHistoryListReturnsData() throws Exception {
        List<HistoryAverage> mockList = List.of(new HistoryAverage(1L, LocalDateTime.now(), "/v1/dinamic-average", "input", "output"));

        when(rateLimitInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(true);

        when(tenpoServiceImpl.getHistoryList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(mockList);

        mockMvc.perform(get("/v1/history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getHistoryReturnsNotFound() throws Exception {
        when(rateLimitInterceptor.preHandle(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(true);
        when(tenpoServiceImpl.getHistoryList(Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/v1/history"))
                .andExpect(status().isNotFound());
    }
}
