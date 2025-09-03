package com.zipcoder.cryptonator_api.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dogecoin_data")
public class Doge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String coinId;  // "dogecoin"
    
    @Column(nullable = false)
    private String symbol;  // "doge"
    
    @Column(nullable = false)
    private String name;    // "Dogecoin"
    
    @Column(nullable = false)
    private Double currentPrice;
    
    @Column(nullable = false)
    private Long marketCap;
    
    @Column(nullable = false)
    private Long totalVolume;
    
    @Column(nullable = false)
    private Double priceChange24h;
    
    @Column(nullable = false)
    private Double priceChangePercentage24h;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    
    @Column(name = "data_fetched_at", nullable = false)
    private LocalDateTime dataFetchedAt;  
    
    // Default constructor 
    public Doge() {}
    
    // Constructor for creating new instances
    public Doge(String coinId, String symbol, String name, Double currentPrice, 
                Long marketCap, Long totalVolume, Double priceChange24h, 
                Double priceChangePercentage24h, LocalDateTime lastUpdated) {
        this.coinId = coinId;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.marketCap = marketCap;
        this.totalVolume = totalVolume;
        this.priceChange24h = priceChange24h;
        this.priceChangePercentage24h = priceChangePercentage24h;
        this.lastUpdated = lastUpdated;
        this.dataFetchedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCoinId() { return coinId; }
    public void setCoinId(String coinId) { this.coinId = coinId; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }
    
    public Long getMarketCap() { return marketCap; }
    public void setMarketCap(Long marketCap) { this.marketCap = marketCap; }
    
    public Long getTotalVolume() { return totalVolume; }
    public void setTotalVolume(Long totalVolume) { this.totalVolume = totalVolume; }
    
    public Double getPriceChange24h() { return priceChange24h; }
    public void setPriceChange24h(Double priceChange24h) { this.priceChange24h = priceChange24h; }
    
    public Double getPriceChangePercentage24h() { return priceChangePercentage24h; }
    public void setPriceChangePercentage24h(Double priceChangePercentage24h) { 
        this.priceChangePercentage24h = priceChangePercentage24h; 
    }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public LocalDateTime getDataFetchedAt() { return dataFetchedAt; }
    public void setDataFetchedAt(LocalDateTime dataFetchedAt) { this.dataFetchedAt = dataFetchedAt; }
    
    @Override
    public String toString() {
        return "Doge{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", priceChange24h=" + priceChange24h +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
