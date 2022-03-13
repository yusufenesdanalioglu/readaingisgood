package com.readingisgood.app.controller;

import com.readingisgood.app.domain.model.Statistics;
import com.readingisgood.app.domain.response.StatisticsResponse;
import com.readingisgood.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    private final OrderService orderService;

    @GetMapping
    public List<StatisticsResponse> get(@RequestParam Long customerId) {
        return orderService.calculateStatistics(customerId);
    }
}
