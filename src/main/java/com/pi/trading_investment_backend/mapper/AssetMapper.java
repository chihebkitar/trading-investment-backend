package com.pi.trading_investment_backend.mapper;

import com.pi.trading_investment_backend.domain.Asset;
import com.pi.trading_investment_backend.dto.AssetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    AssetDTO toDTO(Asset asset);

    Asset toEntity(AssetDTO assetDTO);
}
