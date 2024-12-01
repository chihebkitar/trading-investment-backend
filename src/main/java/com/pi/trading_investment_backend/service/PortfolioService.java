package com.pi.trading_investment_backend.service;

import com.pi.trading_investment_backend.dto.PortfolioDTO;

public interface PortfolioService {

    PortfolioDTO getPortfolioByUserId(Long userId);
}
