package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.domain.*;
import com.pi.trading_investment_backend.dto.TradeOrderDTO;
import com.pi.trading_investment_backend.exception.InsufficientFundsException;
import com.pi.trading_investment_backend.exception.InsufficientHoldingsException;
import com.pi.trading_investment_backend.exception.InvalidOrderException;
import com.pi.trading_investment_backend.exception.ResourceNotFoundException;
import com.pi.trading_investment_backend.mapper.TradeOrderMapper;
import com.pi.trading_investment_backend.repository.*;
import com.pi.trading_investment_backend.service.MarketDataService;
import com.pi.trading_investment_backend.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TradeOrderServiceImpl implements TradeOrderService {




    private final TradeOrderRepository tradeOrderRepository;
    private final PortfolioRepository portfolioRepository;
    private final TradeOrderMapper tradeOrderMapper;
    private final AssetRepository assetRepository;
    private final MarketDataService marketDataService;
    private final UserRepository userRepository;

    @Autowired
    public TradeOrderServiceImpl(TradeOrderRepository tradeOrderRepository,
                                 PortfolioRepository portfolioRepository,
                                 TradeOrderMapper tradeOrderMapper,
                                 AssetRepository assetRepository,
                                 MarketDataService marketDataService,
                                 UserRepository userRepository) {
        this.tradeOrderRepository = tradeOrderRepository;
        this.portfolioRepository = portfolioRepository;
        this.tradeOrderMapper = tradeOrderMapper;
        this.assetRepository = assetRepository;
        this.marketDataService = marketDataService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TradeOrderDTO placeOrder(TradeOrderDTO tradeOrderDTO) {
        // Validate user and asset
        User user = userRepository.findById(tradeOrderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + tradeOrderDTO.getUserId()));
        Asset asset = assetRepository.findById(tradeOrderDTO.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id " + tradeOrderDTO.getAssetId()));

        // Get current market price
        Double marketPrice = marketDataService.getCurrentPrice(asset.getSymbol());

        // Determine execution price
        Double executionPrice;
        if ("MARKET".equalsIgnoreCase(tradeOrderDTO.getOrderType())) {
            executionPrice = marketPrice;
        } else if ("LIMIT".equalsIgnoreCase(tradeOrderDTO.getOrderType())) {
            executionPrice = tradeOrderDTO.getPrice();
            if (executionPrice == null || executionPrice <= 0) {
                throw new InvalidOrderException("Limit orders must have a valid positive price.");
            }
            // For simplicity, assume limit orders execute immediately if market price meets the limit price
            if ("BUY".equalsIgnoreCase(tradeOrderDTO.getSide()) && executionPrice < marketPrice) {
                throw new InvalidOrderException("Market price is higher than limit price.");
            }
            if ("SELL".equalsIgnoreCase(tradeOrderDTO.getSide()) && executionPrice > marketPrice) {
                throw new InvalidOrderException("Market price is lower than limit price.");
            }
        } else {
            throw new InvalidOrderException("Invalid order type. Must be MARKET or LIMIT.");
        }

        // Create trade order entity
        TradeOrder tradeOrder = tradeOrderMapper.toEntity(tradeOrderDTO);
        tradeOrder.setStatus("EXECUTED");
        tradeOrder.setTimestamp(LocalDateTime.now());
        tradeOrder.setPrice(executionPrice);

        // Update portfolio
        Portfolio portfolio = portfolioRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Portfolio newPortfolio = new Portfolio();
                    newPortfolio.setUser(user);
                    newPortfolio.setHoldings(new HashMap<>());
                    return portfolioRepository.save(newPortfolio);
                });

        if ("BUY".equalsIgnoreCase(tradeOrderDTO.getSide())) {
            // Validate funds (Assuming user has a balance field)
            double totalCost = executionPrice * tradeOrderDTO.getQuantity();
            if (user.getBalance() < totalCost) {
                throw new InsufficientFundsException("User has insufficient funds.");
            }
            // Deduct funds
            user.setBalance(user.getBalance() - totalCost);
            userRepository.save(user);

            // Add asset to portfolio
            portfolio.addAsset(asset, tradeOrderDTO.getQuantity());
        } else if ("SELL".equalsIgnoreCase(tradeOrderDTO.getSide())) {
            // Validate holdings
            Integer currentQuantity = portfolio.getHoldings().getOrDefault(asset, 0);
            if (currentQuantity < tradeOrderDTO.getQuantity()) {
                throw new InsufficientHoldingsException("User does not have enough shares to sell.");
            }
            // Add funds
            double totalProceeds = executionPrice * tradeOrderDTO.getQuantity();
            user.setBalance(user.getBalance() + totalProceeds);
            userRepository.save(user);

            // Remove asset from portfolio
            portfolio.removeAsset(asset, tradeOrderDTO.getQuantity());
        } else {
            throw new InvalidOrderException("Invalid order side. Must be BUY or SELL.");
        }

        // Save portfolio and trade order
        portfolioRepository.save(portfolio);
        tradeOrderRepository.save(tradeOrder);

        return tradeOrderMapper.toDTO(tradeOrder);
    }

    @Override
    public List<TradeOrderDTO> getOrdersByUserId(Long userId) {
        List<TradeOrder> orders = tradeOrderRepository.findByUserId(userId);
        return orders.stream().map(tradeOrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TradeOrderDTO getOrderById(Long id) {
        TradeOrder order = tradeOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return tradeOrderMapper.toDTO(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        TradeOrder order = tradeOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Only pending orders can be cancelled");
        }

        order.setStatus("CANCELLED");
        tradeOrderRepository.save(order);
    }
}
