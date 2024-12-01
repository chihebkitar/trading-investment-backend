package com.pi.trading_investment_backend.repository;

import com.pi.trading_investment_backend.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findBySymbol(String symbol);

    Optional<Asset> findByName(String name);

    List<Asset> findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(String nameKeyword, String symbolKeyword);
}
