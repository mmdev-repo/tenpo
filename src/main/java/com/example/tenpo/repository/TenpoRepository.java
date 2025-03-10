
package com.example.tenpo.repository;

import com.example.tenpo.model.HistoryAverage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenpoRepository extends PagingAndSortingRepository<HistoryAverage, Long> {

    void save(HistoryAverage entity);
}

