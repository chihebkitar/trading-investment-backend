package com.pi.trading_investment_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_orders")
@Getter
@Setter
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    private String orderType;

    private Integer quantity;

    private Double price;

    private String side;

    private String status;

    private LocalDateTime timestamp;

}
