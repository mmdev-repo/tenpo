package com.example.tenpo.repository;

import com.example.tenpo.model.HistoryAverage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenpoRepositoryImpl{

    public Page<HistoryAverage> findAll(Pageable pageable){
        List<HistoryAverage> list = new ArrayList<>();
        list.add(HistoryAverage.builder()
                        .date(LocalDateTime.now())
                        .input("5,5")
                        .output("11")
                        .endpoint("dinamic-average")
                        .build());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Async
    public void save(HistoryAverage historyAverage){

        System.out.print(historyAverage);
    }
}
