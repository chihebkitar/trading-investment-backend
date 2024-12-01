package com.pi.trading_investment_backend.mapper;

import com.pi.trading_investment_backend.domain.Asset;
import com.pi.trading_investment_backend.domain.Portfolio;
import com.pi.trading_investment_backend.domain.User;
import com.pi.trading_investment_backend.dto.AssetDTO;
import com.pi.trading_investment_backend.dto.PortfolioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AssetMapper.class})
public abstract class PortfolioMapper {

    @Autowired
    protected AssetMapper assetMapper;

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    @Mapping(source = "holdings", target = "holdings", qualifiedByName = "mapAssetToAssetDTO")
    public abstract PortfolioDTO toDTO(Portfolio portfolio);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    @Mapping(source = "holdings", target = "holdings", qualifiedByName = "mapAssetDTOToAsset")
    public abstract Portfolio toEntity(PortfolioDTO portfolioDTO);

    @Named("userToUserId")
    public static Long userToUserId(User user) {
        return user != null ? user.getId() : null;
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

    @Named("mapAssetToAssetDTO")
    protected Map<AssetDTO, Integer> mapAssetToAssetDTO(Map<Asset, Integer> holdings) {
        if (holdings == null) {
            return null;
        }
        return holdings.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> assetMapper.toDTO(entry.getKey()),
                        Map.Entry::getValue
                ));
    }

    @Named("mapAssetDTOToAsset")
    protected Map<Asset, Integer> mapAssetDTOToAsset(Map<AssetDTO, Integer> holdings) {
        if (holdings == null) {
            return null;
        }
        return holdings.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> assetMapper.toEntity(entry.getKey()),
                        Map.Entry::getValue
                ));
    }
}
