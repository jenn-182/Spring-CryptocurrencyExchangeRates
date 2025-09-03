package com.zipcoder.cryptonator_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinGeckoResponse {
    
    private String id;
    private String symbol;
    private String name;
    
    @JsonProperty("current_price")
    private Double currentPrice;
    
    @JsonProperty("market_cap")
    private Long marketCap;
    
    @JsonProperty("total_volume")
    private Long totalVolume;
    
    @JsonProperty("price_change_24h")
    private Double priceChange24h;
    
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;
    
    @JsonProperty("last_updated")
    private String lastUpdated;  
    
    // Default constructor
    public CoinGeckoResponse() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
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
    
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    
    @Override
    public String toString() {
        return "CoinGeckoResponse{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", marketCap=" + marketCap +
                ", totalVolume=" + totalVolume +
                ", priceChange24h=" + priceChange24h +
                ", priceChangePercentage24h=" + priceChangePercentage24h +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
