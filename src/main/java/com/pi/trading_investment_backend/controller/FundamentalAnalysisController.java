package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.dto.CompanyOverviewDTO;
import com.pi.trading_investment_backend.service.FundamentalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fundamentals")
public class FundamentalAnalysisController {

    private final FundamentalAnalysisService fundamentalAnalysisService;

    @Autowired
    public FundamentalAnalysisController(FundamentalAnalysisService fundamentalAnalysisService) {
        this.fundamentalAnalysisService = fundamentalAnalysisService;
    }

    @GetMapping("/company-overview")
    public ResponseEntity<CompanyOverviewDTO> getCompanyOverview(@RequestParam String symbol) {
        CompanyOverviewDTO overview = fundamentalAnalysisService.getCompanyOverview(symbol);
        return ResponseEntity.ok(overview);
    }
}

