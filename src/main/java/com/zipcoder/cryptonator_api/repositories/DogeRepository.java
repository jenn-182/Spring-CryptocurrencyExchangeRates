package com.zipcoder.cryptonator_api.repositories;

import com.zipcoder.cryptonator_api.domain.Doge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogeRepository extends JpaRepository<Doge, Long> {

    // Find Dogecoin by symbol 
    Optional<Doge> findBySymbol(String symbol);

    // Find Dogecoin by coinId
    Optional<Doge> findByCoinId(String coinId);
    
    // Get the latest Dogecoin entry (most recent data)
    @Query("SELECT d FROM Doge d ORDER BY d.dataFetchedAt DESC")
    Optional<Doge> findLatestDogecoin();
    
    // Get all Dogecoin entries 
    List<Doge> findAllByOrderByDataFetchedAtDesc();
    
    // Check if we have any Dogecoin data
    boolean existsByCoinId(String coinId);
    
    // Find by exact symbol and coinId 
    @Query("SELECT d FROM Doge d WHERE d.symbol = :symbol AND d.coinId = :coinId")
    Optional<Doge> findBySymbolAndCoinId(@Param("symbol") String symbol, @Param("coinId") String coinId);
}
