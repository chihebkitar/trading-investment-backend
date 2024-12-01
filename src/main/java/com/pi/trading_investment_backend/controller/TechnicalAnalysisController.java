package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.service.TechnicalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class TechnicalAnalysisController {

    private final TechnicalAnalysisService technicalAnalysisService;

    @Autowired
    public TechnicalAnalysisController(TechnicalAnalysisService technicalAnalysisService) {
        this.technicalAnalysisService = technicalAnalysisService;
    }

    @GetMapping("/moving-average")
    public ResponseEntity<List<Double>> getMovingAverage(@RequestParam String symbol, @RequestParam int period) {
        List<Double> movingAverages = technicalAnalysisService.calculateMovingAverage(symbol, period);
        return ResponseEntity.ok(movingAverages);
    }

}
