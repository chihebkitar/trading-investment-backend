package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.dto.TradeOrderDTO;
import com.pi.trading_investment_backend.service.TradeOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class TradeOrderController {

    private final TradeOrderService tradeOrderService;

    @Autowired
    public TradeOrderController(TradeOrderService tradeOrderService) {
        this.tradeOrderService = tradeOrderService;
    }

    /**
     * Place a new trade order.
     *
     * @param tradeOrderDTO TradeOrderDTO
     * @return Created TradeOrderDTO
     */
    @PostMapping
    public ResponseEntity<TradeOrderDTO> placeOrder(@Valid @RequestBody TradeOrderDTO tradeOrderDTO) {
        TradeOrderDTO createdOrder = tradeOrderService.placeOrder(tradeOrderDTO);
        return ResponseEntity.ok(createdOrder);
    }

    /**
     * Get orders by user ID.
     *
     * @param userId User ID
     * @return List of TradeOrderDTO
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TradeOrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<TradeOrderDTO> orders = tradeOrderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order details by order ID.
     *
     * @param id Order ID
     * @return TradeOrderDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<TradeOrderDTO> getOrderById(@PathVariable Long id) {
        TradeOrderDTO orderDTO = tradeOrderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Cancel an order.
     *
     * @param id Order ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        tradeOrderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
