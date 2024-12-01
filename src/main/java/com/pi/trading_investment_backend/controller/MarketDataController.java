package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.dto.AlphaVantageTimeSeriesResponse;
import com.pi.trading_investment_backend.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/market-data")
public class MarketDataController {

    private final MarketDataService marketDataService;

    @Autowired
    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/historical")
    public ResponseEntity<AlphaVantageTimeSeriesResponse> getHistoricalData(@RequestParam String symbol) {
        AlphaVantageTimeSeriesResponse data = marketDataService.getTimeSeriesDaily(symbol);
        return ResponseEntity.ok(data);
    }
}
