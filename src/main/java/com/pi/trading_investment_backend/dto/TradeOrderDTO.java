package com.pi.trading_investment_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class TradeOrderDTO {

    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    @NotNull(message = "Asset ID is mandatory")
    private Long assetId;

    @NotBlank(message = "Order side is mandatory")
    @Pattern(regexp = "BUY|SELL", message = "Order side must be BUY or SELL")
    private String side;

    @NotBlank(message = "Order type is mandatory")
    @Pattern(regexp = "MARKET|LIMIT", message = "Order type must be MARKET or LIMIT")
    private String orderType;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private Double price;

    private String status;

    private LocalDateTime timestamp;

}
