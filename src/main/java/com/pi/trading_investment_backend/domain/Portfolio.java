package com.pi.trading_investment_backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "portfolio_holdings", joinColumns = @JoinColumn(name = "portfolio_id"))
    @MapKeyJoinColumn(name = "asset_id")
    @Column(name = "quantity")
    private Map<Asset, Integer> holdings;

    public void addAsset(Asset asset, int quantity) {
        holdings.merge(asset, quantity, Integer::sum);
    }

    public void removeAsset(Asset asset, int quantity) {
        holdings.computeIfPresent(asset, (k, v) -> {
            int updatedQuantity = v - quantity;
            return updatedQuantity > 0 ? updatedQuantity : null;
        });

    }
}
