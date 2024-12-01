package com.pi.trading_investment_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class AssetDTO {

    private Long id;

    @NotBlank(message = "Symbol is mandatory")
    @Size(max = 10, message = "Symbol cannot exceed 10 characters")
    private String symbol;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Type is mandatory")
    private String type;

    private Double currentPrice;
}
