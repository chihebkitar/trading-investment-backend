package com.pi.trading_investment_backend.mapper;

import com.pi.trading_investment_backend.domain.Asset;
import com.pi.trading_investment_backend.domain.TradeOrder;
import com.pi.trading_investment_backend.domain.User;
import com.pi.trading_investment_backend.dto.TradeOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TradeOrderMapper {

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    @Mapping(source = "asset", target = "assetId", qualifiedByName = "assetToAssetId")
    TradeOrderDTO toDTO(TradeOrder tradeOrder);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "assetId", target = "asset", qualifiedByName = "assetIdToAsset")
    TradeOrder toEntity(TradeOrderDTO tradeOrderDTO);

    @Named("userToUserId")
    public static Long userToUserId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("assetToAssetId")
    public static Long assetToAssetId(Asset asset) {
        return asset != null ? asset.getId() : null;
    }

    @Named("userIdToUser")
    public static User userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("assetIdToAsset")
    public static Asset assetIdToAsset(Long assetId) {
        if (assetId == null) {
            return null;
        }
        Asset asset = new Asset();
        asset.setId(assetId);
        return asset;
    }
}
