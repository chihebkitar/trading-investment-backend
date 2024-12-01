package com.pi.trading_investment_backend.service;

import com.pi.trading_investment_backend.dto.CompanyOverviewDTO;

public interface FundamentalAnalysisService {
    CompanyOverviewDTO getCompanyOverview(String symbol);
}
