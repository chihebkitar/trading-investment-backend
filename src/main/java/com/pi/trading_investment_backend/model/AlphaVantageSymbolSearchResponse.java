package com.pi.trading_investment_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.trading_investment_backend.dto.SymbolSearchDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlphaVantageSymbolSearchResponse {

    @JsonProperty("bestMatches")
    private List<BestMatch> bestMatches;

    @Data
    public static class BestMatch {
        @JsonProperty("1. symbol")
        private String symbol;

        @JsonProperty("2. name")
        private String name;

        @JsonProperty("3. type")
        private String type;

        @JsonProperty("4. region")
        private String region;

        @JsonProperty("5. marketOpen")
        private String marketOpen;

        @JsonProperty("6. marketClose")
        private String marketClose;

        @JsonProperty("7. timezone")
        private String timezone;

        @JsonProperty("8. currency")
        private String currency;

        @JsonProperty("9. matchScore")
        private String matchScore;

        public SymbolSearchDTO toSymbolSearchDTO() {
            SymbolSearchDTO dto = new SymbolSearchDTO();
            dto.setSymbol(this.symbol);
            dto.setName(this.name);
            dto.setType(this.type);
            dto.setRegion(this.region);
            dto.setCurrency(this.currency);
            return dto;
        }
    }

    public List<SymbolSearchDTO> toSymbolSearchDTOList() {
        return bestMatches.stream().map(match -> {
            SymbolSearchDTO dto = new SymbolSearchDTO();
            dto.setSymbol(match.getSymbol());
            dto.setName(match.getName());
            dto.setType(match.getType());
            dto.setRegion(match.getRegion());
            dto.setCurrency(match.getCurrency());
            return dto;
        }).collect(Collectors.toList());
    }

}
