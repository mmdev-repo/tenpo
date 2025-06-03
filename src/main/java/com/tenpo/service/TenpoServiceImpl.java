package com.tenpo.service;

import com.tenpo.externalservice.ExternalServiceClient;
import com.tenpo.model.HistoryAverage;
import com.tenpo.repositoryjpa.TenpoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TenpoServiceImpl {

    @Autowired
    private ExternalServiceClient externalServiceClient;

    @Autowired
    private TenpoRepository tenpoRepository;

    public double getDinamicAverage(int num1, int num2) {
        double externalValue = externalServiceClient.getValueFromExternalService();
        double percentage = ((num1 + num2) * externalValue) / 100;
        return ((num2 + num2) + percentage);
    }

    public List<HistoryAverage> getHistoryList(int page, int size){
        Pageable pageRepository = PageRequest.of(page, size);
        Page<HistoryAverage> pageResult = tenpoRepository.findAll(pageRepository);
        return pageResult.toList();
    }

    @Async
    public void saveHistoryAverage(HistoryAverage historyAverage){
        tenpoRepository.save(historyAverage);
    }

}
