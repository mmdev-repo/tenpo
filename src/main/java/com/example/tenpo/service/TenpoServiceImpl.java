package com.example.tenpo.service;

import com.example.tenpo.externalservice.ExternalServiceClient;
import com.example.tenpo.model.HistoryAverage;
import com.example.tenpo.repository.TenpoRepository;
import com.example.tenpo.repository.TenpoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenpoServiceImpl {

    @Autowired
    private ExternalServiceClient externalServiceClient;

    @Autowired
    private TenpoRepositoryImpl tenpoRepositoryImpl;

    public int getDinamicAverage(int num1, int num2){
        int externalValue = externalServiceClient.getValueFromExternalService();
        int percentage = ((num1 + num2)/externalValue);

        return ((num2 + num2) + percentage);
    }

    public List<HistoryAverage> getHistoryList(int page, int size){
        Pageable pageRepository = PageRequest.of(page, size);
        Page<HistoryAverage> pageResult = tenpoRepositoryImpl.findAll(pageRepository);
        return pageResult.toList();
    }

}
