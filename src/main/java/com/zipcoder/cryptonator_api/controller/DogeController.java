package com.zipcoder.cryptonator_api.controller;

import com.zipcoder.cryptonator_api.domain.Doge;
import com.zipcoder.cryptonator_api.services.DogeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doge")
@CrossOrigin(origins = "*")
public class DogeController {
    
    private static final Logger logger = LoggerFactory.getLogger(DogeController.class);
    
    @Autowired
    private DogeService dogeService;
 
    //GET /api/doge/current_price 
    @GetMapping("/current_price")
    public ResponseEntity<?> getCurrentPrice() {
        try {
            Double currentPrice = dogeService.getCurrentPrice();
            
            if (currentPrice != null) {
                logger.info("Returning current Dogecoin price: ${}", currentPrice);
                return ResponseEntity.ok(currentPrice);
            } else {
                // Return cached data message if no price available
                String cacheMessage = dogeService.getCacheStatusMessage();
                Map<String, String> response = new HashMap<>();
                response.put("error", "No current price available");
                response.put("message", cacheMessage);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error getting current price: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to retrieve current price");
            response.put("message", dogeService.getCacheStatusMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    //GET /api/doge/{symbol} 
    @GetMapping("/{symbol}")
    public ResponseEntity<?> getDogeBySymbol(@PathVariable String symbol) {
        try {
            logger.info("Getting Dogecoin data for symbol: {}", symbol);
            Optional<Doge> doge = dogeService.getDogeBySymbol(symbol);
            
            if (doge.isPresent()) {
                return ResponseEntity.ok(doge.get());
            } else {
                // Check if we have any cached data
                Optional<Doge> latestDoge = dogeService.getLatestDoge();
                if (latestDoge.isPresent() && "doge".equalsIgnoreCase(symbol)) {
                    // Return cached dogecoin data with warning message
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", latestDoge.get());
                    response.put("message", dogeService.getCacheStatusMessage());
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "Symbol '" + symbol + "' not found");
                    response.put("message", "Available symbol: 'doge'");
                    return ResponseEntity.notFound().build();
                }
            }
            
        } catch (Exception e) {
            logger.error("Error getting data for symbol {}: {}", symbol, e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to retrieve data for symbol: " + symbol);
            response.put("message", dogeService.getCacheStatusMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    

    //GET /api/doge/latest - Get the latest Dogecoin data
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestDoge() {
        try {
            Optional<Doge> latestDoge = dogeService.getLatestDoge();
            
            if (latestDoge.isPresent()) {
                return ResponseEntity.ok(latestDoge.get());
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "No Dogecoin data available");
                response.put("message", "Try the /fetch endpoint to get fresh data");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error getting latest Dogecoin data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving latest data");
        }
    }

    //POST /api/doge/fetch - Manually fetch fresh data from CoinGecko API
    @PostMapping("/fetch")
    public ResponseEntity<?> fetchFreshData() {
        try {
            logger.info("Manual fetch request received");
            Doge freshDoge = dogeService.fetchAndSaveLatestDoge();
            
            if (freshDoge != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Successfully fetched fresh Dogecoin data");
                response.put("data", freshDoge);
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Failed to fetch fresh data");
                response.put("message", dogeService.getCacheStatusMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error during manual fetch: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to fetch fresh data");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //GET /api/doge/health
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Dogecoin API");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
  
        Optional<Doge> latestDoge = dogeService.getLatestDoge();
        if (latestDoge.isPresent()) {
            health.put("data_available", true);
            health.put("last_update", latestDoge.get().getDataFetchedAt().toString());
            health.put("current_price", latestDoge.get().getCurrentPrice());
        } else {
            health.put("data_available", false);
            health.put("message", "No data available - try /fetch endpoint");
        }
        
        return ResponseEntity.ok(health);
    }
}
