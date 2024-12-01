package com.pi.trading_investment_backend.service;

import com.pi.trading_investment_backend.dto.AlphaVantageTimeSeriesResponse;
import com.pi.trading_investment_backend.dto.MarketDataDTO;

public interface MarketDataService {

    double getCurrentPrice(String symbol);

    MarketDataDTO getMarketData(String symbol);
    AlphaVantageTimeSeriesResponse getTimeSeriesDaily(String symbol);

}
