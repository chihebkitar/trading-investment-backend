package com.pi.trading_investment_backend.dto;

import lombok.Data;

@Data
public class SymbolSearchDTO {

    private String symbol;
    private String name;
    private String type;
    private String region;
    private String currency;
    public AssetDTO toAssetDTO() {
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setSymbol(this.symbol);
        assetDTO.setName(this.name);
        assetDTO.setType(this.type);
        return assetDTO;
    }

}
