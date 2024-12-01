package com.pi.trading_investment_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "market_data")
@Data
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private LocalDate date;

    private Double openPrice;

    private Double closePrice;

    private Double highPrice;

    private Double lowPrice;

    private Long volume;

}
