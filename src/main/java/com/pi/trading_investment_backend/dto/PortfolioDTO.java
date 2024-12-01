package com.pi.trading_investment_backend.dto;

import lombok.Data;
import java.util.Map;
import jakarta.validation.constraints.NotNull;

@Data
public class PortfolioDTO {

    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    private Map<AssetDTO, Integer> holdings;

    private Double totalValue;
}
