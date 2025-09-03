package com.zipcoder.cryptonator_api.services;

import com.zipcoder.cryptonator_api.domain.Doge;
import com.zipcoder.cryptonator_api.domain.CoinGeckoResponse;
import com.zipcoder.cryptonator_api.repositories.DogeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DogeService {
    
    private static final Logger logger = LoggerFactory.getLogger(DogeService.class);
    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=dogecoin&x_cg_demo_api_key=CG-ynA2Cg5oeTj9LF6zfMjMJCHV";
    
    @Autowired
    private DogeRepository dogeRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    // GET/api/doge/current_price
    public Double getCurrentPrice() {
        Optional<Doge> latestDoge = dogeRepository.findLatestDogecoin();
        return latestDoge.map(Doge::getCurrentPrice).orElse(null);
    }
    // GET/api/doge/{symbol} 
    public Optional<Doge> getDogeBySymbol(String symbol) {
        return dogeRepository.findBySymbol(symbol.toLowerCase());
    }
    // GET/api/doge/latest
    public Optional<Doge> getLatestDoge() {
        return dogeRepository.findLatestDogecoin();
    }
    
    // GET/api/doge/all
    public List<Doge> getAllDoge() {
        return dogeRepository.findAllByOrderByDataFetchedAtDesc();
    }
    
    //fetch and save fresh Dogecoin data from CoinGecko API
    public Doge fetchAndSaveLatestDoge() {
        try {
            logger.info("Manually fetching latest Dogecoin data from CoinGecko API");
            return fetchFromApiAndSave();
        } catch (Exception e) {
            logger.error("Manual fetch failed: {}", e.getMessage());
            return null;
        }
    }
    
    // Automatically update Dogecoin data every 5 minutes (300,000 milliseconds)
    @Scheduled(fixedRate = 300000)
    public void updateDogeDataScheduled() {
        try {
            logger.info("Scheduled update: Fetching Dogecoin data from CoinGecko API");
            Doge updatedDoge = fetchFromApiAndSave();
            if (updatedDoge != null) {
                logger.info("Successfully updated Dogecoin data. Price: ${}", updatedDoge.getCurrentPrice());
            }
        } catch (Exception e) {
            logger.error("Scheduled update failed: {}", e.getMessage());
            logger.info("Will use cached data until next update attempt");
        }
    }
    
    //Fetch data from CoinGecko API and save to database
    private Doge fetchFromApiAndSave() {
        try {
            logger.info("Calling CoinGecko API: {}", COINGECKO_API_URL);
            
            // CoinGecko returns an array, so we get the first element
            CoinGeckoResponse[] responseArray = restTemplate.getForObject(COINGECKO_API_URL, CoinGeckoResponse[].class);
            
            if (responseArray == null || responseArray.length == 0) {
                logger.error("No data received from CoinGecko API");
                return null;
            }
            
            CoinGeckoResponse response = responseArray[0];
            logger.info("Received data for: {} ({})", response.getName(), response.getSymbol());
            
            // Convert API response to our Doge entity
            Doge doge = convertResponseToDoge(response);
            
            //delete old entries first
            deleteOldDogeData();
            
            // Save new data
            Doge savedDoge = dogeRepository.save(doge);
            logger.info("Saved Dogecoin data with ID: {}", savedDoge.getId());
            
            return savedDoge;
            
        } catch (Exception e) {
            logger.error("Error fetching data from CoinGecko API: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    //Convert CoinGecko API response to our Doge entity
    private Doge convertResponseToDoge(CoinGeckoResponse response) {
        // Parse the ISO timestamp
        LocalDateTime lastUpdated = parseTimestamp(response.getLastUpdated());
        
        return new Doge(
            response.getId(),                        
            response.getSymbol(),                     
            response.getName(),                        
            response.getCurrentPrice(),                 
            response.getMarketCap(),                    
            response.getTotalVolume(),                  
            response.getPriceChange24h(),               
            response.getPriceChangePercentage24h(),     
            lastUpdated                                 
        );
    }
    
    //Parse ISO timestamp from CoinGecko to LocalDateTime
    private LocalDateTime parseTimestamp(String timestamp) {
        try {
            if (timestamp == null || timestamp.isEmpty()) {
                return LocalDateTime.now();
            }
            // CoinGecko returns ISO format
            return LocalDateTime.parse(timestamp.replace("Z", ""), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        } catch (Exception e) {
            logger.warn("Could not parse timestamp '{}', using current time", timestamp);
            return LocalDateTime.now();
        }
    }
    
    //Delete old Dogecoin data (keep only latest)
    private void deleteOldDogeData() {
        try {
            List<Doge> allDoge = dogeRepository.findAll();
            if (!allDoge.isEmpty()) {
                dogeRepository.deleteAll(allDoge);
                logger.info("Deleted {} old Dogecoin entries", allDoge.size());
            }
        } catch (Exception e) {
            logger.warn("Could not delete old data: {}", e.getMessage());
        }
    }

    //Get cache status message for error responses
    public String getCacheStatusMessage() {
        Optional<Doge> latestDoge = dogeRepository.findLatestDogecoin();
        if (latestDoge.isPresent()) {
            return "API temporarily unavailable. Showing cached data from " + 
                   latestDoge.get().getDataFetchedAt().toString();
        } else {
            return "No data available";
        }
    }
}
