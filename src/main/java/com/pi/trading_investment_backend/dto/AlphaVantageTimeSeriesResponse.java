package com.pi.trading_investment_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class AlphaVantageTimeSeriesResponse {

    @JsonProperty("Meta Data")
    private MetaData metaData;

    @JsonProperty("Time Series (Daily)")
    private Map<String, TimeSeriesData> timeSeries;

    @Data
    public static class MetaData {
        @JsonProperty("1. Information")
        private String information;

        @JsonProperty("2. Symbol")
        private String symbol;

        @JsonProperty("3. Last Refreshed")
        private String lastRefreshed;

        @JsonProperty("4. Output Size")
        private String outputSize;

        @JsonProperty("5. Time Zone")
        private String timeZone;
    }

    @Data
    public static class TimeSeriesData {
        @JsonProperty("1. open")
        private String open;

        @JsonProperty("2. high")
        private String high;

        @JsonProperty("3. low")
        private String low;

        @JsonProperty("4. close")
        private String close;

        @JsonProperty("5. adjusted close")
        private String adjustedClose;

        @JsonProperty("6. volume")
        private String volume;

        @JsonProperty("7. dividend amount")
        private String dividendAmount;

        @JsonProperty("8. split coefficient")
        private String splitCoefficient;
    }
}
