package com.pi.trading_investment_backend.service;

import com.pi.trading_investment_backend.dto.AssetDTO;

import java.util.List;

public interface AssetService {

    AssetDTO getAssetById(Long id);

    AssetDTO getAssetBySymbol(String symbol);

    List<AssetDTO> searchAssets(String keyword);

    AssetDTO createAsset(AssetDTO assetDTO);

    AssetDTO updateAsset(Long id, AssetDTO assetDTO);

    void deleteAsset(Long id);
}
