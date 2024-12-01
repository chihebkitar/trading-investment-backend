package com.pi.trading_investment_backend.service;

import com.pi.trading_investment_backend.dto.TradeOrderDTO;

import java.util.List;

public interface TradeOrderService {

    TradeOrderDTO placeOrder(TradeOrderDTO tradeOrderDTO);

    List<TradeOrderDTO> getOrdersByUserId(Long userId);

    TradeOrderDTO getOrderById(Long id);

    void cancelOrder(Long id);
}
