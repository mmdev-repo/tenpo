package com.example.tenpo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryAverage {

    private LocalDateTime date;
    private String endpoint;
    private String input;
    private String output;
}
