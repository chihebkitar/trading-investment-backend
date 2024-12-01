package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.domain.Asset;
import com.pi.trading_investment_backend.domain.Portfolio;
import com.pi.trading_investment_backend.dto.PortfolioDTO;
import com.pi.trading_investment_backend.exception.ResourceNotFoundException;
import com.pi.trading_investment_backend.mapper.PortfolioMapper;
import com.pi.trading_investment_backend.repository.PortfolioRepository;
import com.pi.trading_investment_backend.service.MarketDataService;
import com.pi.trading_investment_backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PortfolioServiceImpl implements PortfolioService {


    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;
    private final MarketDataService marketDataService;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository,
                                PortfolioMapper portfolioMapper,
                                MarketDataService marketDataService) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
        this.marketDataService = marketDataService;
    }

    @Override
    public PortfolioDTO getPortfolioByUserId(Long userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user id " + userId));

        PortfolioDTO portfolioDTO = portfolioMapper.toDTO(portfolio);

        // Calculate total value
        double totalValue = portfolio.getHoldings().entrySet().stream()
                .mapToDouble(entry -> {
                    Asset asset = entry.getKey();
                    Integer quantity = entry.getValue();
                    Double currentPrice = marketDataService.getCurrentPrice(asset.getSymbol());
                    return currentPrice * quantity;
                })
                .sum();

        portfolioDTO.setTotalValue(totalValue);

        // Set current prices in holdings
        portfolioDTO.getHoldings().forEach((assetDTO, quantity) -> {
            Double currentPrice = marketDataService.getCurrentPrice(assetDTO.getSymbol());
            assetDTO.setCurrentPrice(currentPrice);
        });

        return portfolioDTO;
    }

}
