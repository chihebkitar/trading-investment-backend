package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.client.AlphaVantageClient;
import com.pi.trading_investment_backend.dto.AlphaVantageTimeSeriesResponse;
import com.pi.trading_investment_backend.dto.MarketDataDTO;
import com.pi.trading_investment_backend.exception.ExternalApiException;
import com.pi.trading_investment_backend.service.MarketDataService;
import org.springframework.stereotype.Service;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final AlphaVantageClient alphaVantageClient;

    public MarketDataServiceImpl(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }

    @Override
    public double getCurrentPrice(String symbol) {
        MarketDataDTO marketData = getMarketData(symbol);
        if (marketData.getPrice() != null) {
            return marketData.getPrice();
        } else {
            throw new ExternalApiException("Current price not available for symbol: " + symbol);
        }
    }

    @Override
    public MarketDataDTO getMarketData(String symbol) {
        return alphaVantageClient.getRealTimeQuote(symbol);
    }

    @Override
    public AlphaVantageTimeSeriesResponse getTimeSeriesDaily(String symbol) {
        return alphaVantageClient.getTimeSeriesDaily(symbol);
    }
}
