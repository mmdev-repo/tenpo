package com.example.tenpo.controller;

import com.example.tenpo.model.HistoryAverage;
import com.example.tenpo.service.TenpoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class TenpoController {

    @Autowired
    private TenpoServiceImpl tenpoServiceImpl;


    @GetMapping("/dinamic-average")
    public String getDinamicAverage(@RequestParam int num1, @RequestParam int num2) {
        return Integer.toString(tenpoServiceImpl.getDinamicAverage(num1, num2));
    }

    @GetMapping("/history")
    public List<HistoryAverage> getHistoryList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<HistoryAverage> list = tenpoServiceImpl.getHistoryList(page, size);
        return list;
    }
}
