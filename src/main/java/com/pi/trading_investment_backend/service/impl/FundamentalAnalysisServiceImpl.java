package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.client.AlphaVantageClient;
import com.pi.trading_investment_backend.dto.CompanyOverviewDTO;
import com.pi.trading_investment_backend.service.FundamentalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundamentalAnalysisServiceImpl implements FundamentalAnalysisService {

    private final AlphaVantageClient alphaVantageClient;

    @Autowired
    public FundamentalAnalysisServiceImpl(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }

    @Override
    public CompanyOverviewDTO getCompanyOverview(String symbol) {
        return alphaVantageClient.getCompanyOverview(symbol);
    }
}
