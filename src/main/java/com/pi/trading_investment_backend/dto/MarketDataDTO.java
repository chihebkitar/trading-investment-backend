package com.pi.trading_investment_backend.dto;

import lombok.Data;

@Data
public class MarketDataDTO {

    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double price;
    private Long volume;
    private String latestTradingDay;
    private Double previousClose;
    private Double change;
    private String changePercent;

}
