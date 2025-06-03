package com.tenpo.controller;

import com.tenpo.aop.SaveRequestInfo;
import com.tenpo.exception.InvalidParameterException;
import com.tenpo.exception.ResourceNotFoundException;
import com.tenpo.model.HistoryAverage;
import com.tenpo.service.TenpoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Tag(name = "dinamic-average", description = "dinamic-average API")
public class TenpoController {

    @Autowired
    private TenpoServiceImpl tenpoServiceImpl;


    @GetMapping("/dinamic-average")
    @SaveRequestInfo
    public ResponseEntity<Double> getDinamicAverage(@RequestParam int num1, @RequestParam int num2) {
        if (num1 < 0 || num2 < 0) {
            throw new InvalidParameterException("Parameters sent need to be positive values.");
        }
        double result = tenpoServiceImpl.getDinamicAverage(num1, num2);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/history")
    public List<HistoryAverage> getHistoryList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<HistoryAverage> list = tenpoServiceImpl.getHistoryList(page, size);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Registers not found.");
        }
        return list;
    }
}
