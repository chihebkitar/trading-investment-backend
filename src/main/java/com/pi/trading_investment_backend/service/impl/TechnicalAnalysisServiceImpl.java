package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.client.AlphaVantageClient;
import com.pi.trading_investment_backend.dto.AlphaVantageTimeSeriesResponse;
import com.pi.trading_investment_backend.service.TechnicalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TechnicalAnalysisServiceImpl implements TechnicalAnalysisService {

    private final AlphaVantageClient alphaVantageClient;

    @Autowired
    public TechnicalAnalysisServiceImpl(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }

    @Override
    public List<Double> calculateMovingAverage(String symbol, int period) {
        AlphaVantageTimeSeriesResponse timeSeries = alphaVantageClient.getTimeSeriesDaily(symbol);

        List<Double> closingPrices = timeSeries.getTimeSeries().values().stream()
                .map(data -> Double.parseDouble(data.getClose()))
                .collect(Collectors.toList());

        List<Double> movingAverages = new ArrayList<>();
        for (int i = 0; i <= closingPrices.size() - period; i++) {
            double sum = 0;
            for (int j = i; j < i + period; j++) {
                sum += closingPrices.get(j);
            }
            movingAverages.add(sum / period);
        }

        return movingAverages;
    }

    @Override
    public List<Double> calculateRSI(String symbol, int period) {
        AlphaVantageTimeSeriesResponse timeSeries = alphaVantageClient.getTimeSeriesDaily(symbol);

        // Ensure data is sorted in chronological order (oldest to newest)
        List<Map.Entry<String, AlphaVantageTimeSeriesResponse.TimeSeriesData>> sortedEntries = timeSeries.getTimeSeries().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        List<Double> closingPrices = sortedEntries.stream()
                .map(entry -> Double.parseDouble(entry.getValue().getClose()))
                .collect(Collectors.toList());

        List<Double> rsiValues = new ArrayList<>();

        // Calculate initial average gain and loss
        double gainSum = 0.0;
        double lossSum = 0.0;
        for (int i = 1; i <= period; i++) {
            double change = closingPrices.get(i) - closingPrices.get(i - 1);
            if (change > 0) {
                gainSum += change;
            } else {
                lossSum += -change; // Loss is positive
            }
        }

        double averageGain = gainSum / period;
        double averageLoss = lossSum / period;

        // Calculate initial RSI value
        double rs = averageLoss == 0 ? 0 : averageGain / averageLoss;
        double rsi = averageLoss == 0 ? 100 : 100 - (100 / (1 + rs));
        rsiValues.add(rsi);

        // Calculate RSI for subsequent periods
        for (int i = period + 1; i < closingPrices.size(); i++) {
            double change = closingPrices.get(i) - closingPrices.get(i - 1);
            double gain = change > 0 ? change : 0;
            double loss = change < 0 ? -change : 0;

            averageGain = ((averageGain * (period - 1)) + gain) / period;
            averageLoss = ((averageLoss * (period - 1)) + loss) / period;

            rs = averageLoss == 0 ? 0 : averageGain / averageLoss;
            rsi = averageLoss == 0 ? 100 : 100 - (100 / (1 + rs));
            rsiValues.add(rsi);
        }

        return rsiValues;
    }
}
