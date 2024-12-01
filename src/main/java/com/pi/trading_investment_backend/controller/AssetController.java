package com.pi.trading_investment_backend.controller;

import com.pi.trading_investment_backend.dto.AssetDTO;
import com.pi.trading_investment_backend.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Get asset details by ID.
     *
     * @param id Asset ID
     * @return AssetDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        AssetDTO assetDTO = assetService.getAssetById(id);
        return ResponseEntity.ok(assetDTO);
    }

    /**
     * Get asset details by symbol.
     *
     * @param symbol Asset symbol
     * @return AssetDTO
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<AssetDTO> getAssetBySymbol(@PathVariable String symbol) {
        AssetDTO assetDTO = assetService.getAssetBySymbol(symbol);
        return ResponseEntity.ok(assetDTO);
    }

    /**
     * Search for assets by keyword.
     *
     * @param keyword Search keyword
     * @return List of AssetDTO
     */
    @GetMapping("/search")
    public ResponseEntity<List<AssetDTO>> searchAssets(@RequestParam String keyword) {
        List<AssetDTO> assets = assetService.searchAssets(keyword);
        return ResponseEntity.ok(assets);
    }

    /**
     * Create a new asset.
     *
     * @param assetDTO AssetDTO
     * @return Created AssetDTO
     */
    @PostMapping
    public ResponseEntity<AssetDTO> createAsset(@Valid @RequestBody AssetDTO assetDTO) {
        AssetDTO createdAsset = assetService.createAsset(assetDTO);
        return ResponseEntity.ok(createdAsset);
    }

    /**
     * Update an existing asset.
     *
     * @param id       Asset ID
     * @param assetDTO AssetDTO
     * @return Updated AssetDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetDTO assetDTO) {
        AssetDTO updatedAsset = assetService.updateAsset(id, assetDTO);
        return ResponseEntity.ok(updatedAsset);
    }

    /**
     * Delete an asset.
     *
     * @param id Asset ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
