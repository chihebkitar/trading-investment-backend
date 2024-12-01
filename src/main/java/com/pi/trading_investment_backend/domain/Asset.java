package com.pi.trading_investment_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "assets")
@Data
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private String name;

    private String type;

    @OneToMany(mappedBy = "asset")
    private Set<TradeOrder> tradeOrders;

}
