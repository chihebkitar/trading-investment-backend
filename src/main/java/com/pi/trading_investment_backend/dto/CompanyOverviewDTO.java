package com.pi.trading_investment_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyOverviewDTO {

    @JsonProperty("Symbol")
    private String symbol;

    @JsonProperty("AssetType")
    private String assetType;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Exchange")
    private String exchange;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Sector")
    private String sector;

    @JsonProperty("Industry")
    private String industry;

    @JsonProperty("MarketCapitalization")
    private String marketCapitalization;

    @JsonProperty("PEGRatio")
    private String pegRatio;

    @JsonProperty("EPS")
    private String eps;

    // Add other fields as needed
}
