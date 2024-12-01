package com.pi.trading_investment_backend.service;

import java.util.List;

public interface TechnicalAnalysisService {
    List<Double> calculateMovingAverage(String symbol, int period);
    List<Double> calculateRSI(String symbol, int period);
}
