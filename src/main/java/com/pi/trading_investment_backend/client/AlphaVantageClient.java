package com.pi.trading_investment_backend.client;

import com.pi.trading_investment_backend.dto.AlphaVantageTimeSeriesResponse;
import com.pi.trading_investment_backend.dto.CompanyOverviewDTO;
import com.pi.trading_investment_backend.dto.MarketDataDTO;
import com.pi.trading_investment_backend.dto.SymbolSearchDTO;
import com.pi.trading_investment_backend.exception.ExternalApiException;
import com.pi.trading_investment_backend.model.AlphaVantageGlobalQuoteResponse;
import com.pi.trading_investment_backend.model.AlphaVantageSymbolSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Component
public class AlphaVantageClient {

    private final RestTemplate restTemplate;

    @Value("${alphavantage.api-key}")
    private String apiKey;

    @Value("${alphavantage.base-url}")
    private String baseUrl;

    public AlphaVantageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches real-time stock quote for the given symbol.
     *
     * @param symbol Stock symbol
     * @return MarketDataDTO containing the latest market data
     */
    @Cacheable(value = "realTimeQuote", key = "#symbol", unless = "#result == null")
    public MarketDataDTO getRealTimeQuote(String symbol) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .build()
                .toUri();

        try {
            AlphaVantageGlobalQuoteResponse response = restTemplate.getForObject(uri, AlphaVantageGlobalQuoteResponse.class);
            if (response != null && response.getGlobalQuote() != null) {
                return mapToMarketDataDTO(response.getGlobalQuote());
            } else {
                throw new ExternalApiException("No data found for symbol: " + symbol);
            }
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch real-time quote: " + e.getMessage(), e);
        }
    }

    /**
     * Fetches historical time series data for the given symbol.
     *
     * @param symbol Stock symbol
     * @return AlphaVantageTimeSeriesResponse containing historical data
     */
    @Cacheable(value = "timeSeriesData", key = "#symbol", unless = "#result == null")
    public AlphaVantageTimeSeriesResponse getTimeSeriesDaily(String symbol) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "TIME_SERIES_DAILY_ADJUSTED")
                .queryParam("symbol", symbol)
                .queryParam("outputsize", "compact")
                .queryParam("apikey", apiKey)
                .build()
                .toUri();

        try {
            AlphaVantageTimeSeriesResponse response = restTemplate.getForObject(uri, AlphaVantageTimeSeriesResponse.class);
            if (response != null && response.getTimeSeries() != null) {
                return response;
            } else {
                throw new ExternalApiException("No historical data found for symbol: " + symbol);
            }
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch historical data: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for assets matching the given keywords.
     *
     * @param keywords Search keywords
     * @return List of SymbolSearchDTO containing matching assets
     */
    @Cacheable(value = "symbolSearch", key = "#keywords", unless = "#result == null || #result.isEmpty()")
    public List<SymbolSearchDTO> searchSymbols(String keywords) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "SYMBOL_SEARCH")
                .queryParam("keywords", keywords)
                .queryParam("apikey", apiKey)
                .build()
                .toUri();

        try {
            AlphaVantageSymbolSearchResponse response = restTemplate.getForObject(uri, AlphaVantageSymbolSearchResponse.class);
            if (response != null && response.getBestMatches() != null) {
                return response.toSymbolSearchDTOList();
            } else {
                throw new ExternalApiException("No matches found for keywords: " + keywords);
            }
        } catch (Exception e) {
            throw new ExternalApiException("Failed to search symbols: " + e.getMessage(), e);
        }
    }

    // Helper method to map API response to MarketDataDTO
    private MarketDataDTO mapToMarketDataDTO(AlphaVantageGlobalQuoteResponse.GlobalQuote quote) {
        MarketDataDTO marketDataDTO = new MarketDataDTO();
        marketDataDTO.setSymbol(quote.getSymbol());
        marketDataDTO.setOpen(parseDouble(quote.getOpen()));
        marketDataDTO.setHigh(parseDouble(quote.getHigh()));
        marketDataDTO.setLow(parseDouble(quote.getLow()));
        marketDataDTO.setPrice(parseDouble(quote.getPrice()));
        marketDataDTO.setVolume(parseLong(quote.getVolume()));
        marketDataDTO.setLatestTradingDay(quote.getLatestTradingDay());
        marketDataDTO.setPreviousClose(parseDouble(quote.getPreviousClose()));
        marketDataDTO.setChange(parseDouble(quote.getChange()));
        marketDataDTO.setChangePercent(quote.getChangePercent());
        return marketDataDTO;
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }
    public SymbolSearchDTO searchSymbol(String symbol) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "SYMBOL_SEARCH")
                .queryParam("keywords", symbol)
                .queryParam("apikey", apiKey)
                .build()
                .toUri();

        try {
            AlphaVantageSymbolSearchResponse response = restTemplate.getForObject(uri, AlphaVantageSymbolSearchResponse.class);
            if (response != null && response.getBestMatches() != null) {
                return response.getBestMatches().stream()
                        .filter(match -> match.getSymbol().equalsIgnoreCase(symbol))
                        .findFirst()
                        .map(AlphaVantageSymbolSearchResponse.BestMatch::toSymbolSearchDTO)
                        .orElse(null);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ExternalApiException("Failed to search symbol: " + e.getMessage(), e);
        }
    }
    public CompanyOverviewDTO getCompanyOverview(String symbol) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "OVERVIEW")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .build()
                .toUri();

        try {
            CompanyOverviewDTO response = restTemplate.getForObject(uri, CompanyOverviewDTO.class);
            if (response != null) {
                return response;
            } else {
                throw new ExternalApiException("No company overview found for symbol: " + symbol);
            }
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch company overview: " + e.getMessage(), e);
        }
    }


}
