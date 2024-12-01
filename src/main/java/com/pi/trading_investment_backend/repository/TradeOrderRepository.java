package com.pi.trading_investment_backend.repository;

import com.pi.trading_investment_backend.domain.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeOrderRepository extends JpaRepository<TradeOrder, Long> {

    List<TradeOrder> findByUserId(Long userId);

    List<TradeOrder> findByAssetId(Long assetId);

    List<TradeOrder> findByUserIdAndStatus(Long userId, String status);
}
