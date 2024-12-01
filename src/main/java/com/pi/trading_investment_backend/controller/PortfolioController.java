package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.dto.PortfolioDTO;
import com.pi.trading_investment_backend.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PortfolioController handles requests related to user portfolios.
 */
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Get portfolio by user ID.
     *
     * @param userId User ID
     * @return PortfolioDTO
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PortfolioDTO> getPortfolioByUserId(@PathVariable Long userId) {
        PortfolioDTO portfolioDTO = portfolioService.getPortfolioByUserId(userId);
        return ResponseEntity.ok(portfolioDTO);
    }
}
