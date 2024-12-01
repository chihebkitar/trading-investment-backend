package com.pi.trading_investment_backend.service.impl;

import com.pi.trading_investment_backend.client.AlphaVantageClient;
import com.pi.trading_investment_backend.domain.Asset;
import com.pi.trading_investment_backend.dto.AssetDTO;
import com.pi.trading_investment_backend.dto.SymbolSearchDTO;
import com.pi.trading_investment_backend.exception.ResourceNotFoundException;
import com.pi.trading_investment_backend.mapper.AssetMapper;
import com.pi.trading_investment_backend.repository.AssetRepository;
import com.pi.trading_investment_backend.service.AssetService;
import com.pi.trading_investment_backend.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final AlphaVantageClient alphaVantageClient;
    private final MarketDataService marketDataService;
    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, AssetMapper assetMapper,
                            AlphaVantageClient alphaVantageClient, MarketDataService marketDataService) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
        this.alphaVantageClient = alphaVantageClient;
        this.marketDataService = marketDataService;
    }

    @Override
    public AssetDTO getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id " + id));
        return assetMapper.toDTO(asset);
    }

    @Override
    public AssetDTO getAssetBySymbol(String symbol) {
        Asset asset = assetRepository.findBySymbol(symbol).orElseGet(() -> {
            // Fetch asset data from Alpha Vantage
            SymbolSearchDTO symbolData = alphaVantageClient.searchSymbol(symbol);
            if (symbolData != null) {
                // Map SymbolSearchDTO to AssetDTO
                AssetDTO assetDTO = new AssetDTO();
                assetDTO.setSymbol(symbolData.getSymbol());
                assetDTO.setName(symbolData.getName());
                assetDTO.setType(symbolData.getType());

                // Fetch current price
                Double currentPrice = marketDataService.getCurrentPrice(symbol);
                assetDTO.setCurrentPrice(currentPrice);

                // Save the new asset to the database
                Asset newAsset = assetRepository.save(assetMapper.toEntity(assetDTO));
                return newAsset;
            } else {
                throw new ResourceNotFoundException("Asset not found with symbol " + symbol);
            }
        });
        AssetDTO assetDTO = assetMapper.toDTO(asset);
        assetDTO.setCurrentPrice(marketDataService.getCurrentPrice(symbol));
        return assetDTO;
    }


    @Override
    public List<AssetDTO> searchAssets(String keyword) {
        List<Asset> assets = assetRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(keyword, keyword);

        if (assets.isEmpty()) {
            // Fetch data from Alpha Vantage
            List<SymbolSearchDTO> symbolSearchResults = alphaVantageClient.searchSymbols(keyword);

            // Map and save new assets
            assets = symbolSearchResults.stream()
                    .map(symbolSearchDTO -> {
                        Asset existingAsset = assetRepository.findBySymbol(symbolSearchDTO.getSymbol()).orElse(null);
                        if (existingAsset == null) {
                            Asset newAsset = assetMapper.toEntity(symbolSearchDTO.toAssetDTO());
                            return assetRepository.save(newAsset);
                        } else {
                            return existingAsset;
                        }
                    })
                    .collect(Collectors.toList());
        }

        return assets.stream()
                .map(assetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDTO createAsset(AssetDTO assetDTO) {
        Asset asset = assetMapper.toEntity(assetDTO);
        Asset savedAsset = assetRepository.save(asset);
        return assetMapper.toDTO(savedAsset);
    }

    @Override
    public AssetDTO updateAsset(Long id, AssetDTO assetDTO) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id " + id));

        existingAsset.setName(assetDTO.getName());
        existingAsset.setSymbol(assetDTO.getSymbol());
        existingAsset.setType(assetDTO.getType());

        Asset updatedAsset = assetRepository.save(existingAsset);
        return assetMapper.toDTO(updatedAsset);
    }

    @Override
    public void deleteAsset(Long id) {
        Asset existingAsset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id " + id));
        assetRepository.delete(existingAsset);
    }
}
