package com.personal.stocksphere.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.personal.stocksphere.dto.StockDto;
import com.personal.stocksphere.entity.Stock;
import com.personal.stocksphere.entity.User;
import com.personal.stocksphere.exceptions.UserDoesNotExistsException;
import com.personal.stocksphere.repository.StockRepository;
import com.personal.stocksphere.repository.UserRepository;
import com.personal.stocksphere.service.StockService;

@Service
public class StockServiceImpl implements StockService{
	
	private String apiKey="UTJEKUP5EKS2FN1Z";

	private final String API_URL = "https://www.alphavantage.co/query";
	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public Stock addStock(StockDto stock, String username) throws UserDoesNotExistsException {
		// TODO Auto-generated method stub
		User resp = this.userRepository.findByUsername(username);
		if(resp!=null) {
			Stock s = new Stock();
			s.setUser(resp);
			s.setName(stock.getName());
			s.setQuantity(stock.getQuantity());
			s.setTicker(stock.getTicker());
			s.setBuyPrice(stock.getBuyPrice());
			
			s = this.stockRepository.save(s);
			
			
			resp.getStocks().add(s);
			this.userRepository.save(resp);
			
			return s;
		}
		else {
			throw new UserDoesNotExistsException("No user with this credentials!!");
		}
	}

	@Override
	public List<Stock> getAllStocks(String username) throws UserDoesNotExistsException {
		// TODO Auto-generated method stub
		User resp = this.userRepository.findByUsername(username);
		if(resp!=null) {
			return resp.getStocks();
		}
		else {
			throw new UserDoesNotExistsException("No user with this credentials!!");
		}
	}

	@Override
	public Stock updateStock(Long id, StockDto updatedStock, String username) {
		// TODO Auto-generated method stub
		Stock stock = stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock not found")); 
		stock.setName(updatedStock.getName()); 
		stock.setTicker(updatedStock.getTicker()); 
		stock.setQuantity(updatedStock.getQuantity()); 
		stock.setBuyPrice(updatedStock.getBuyPrice()); 
		return stockRepository.save(stock);
	}

	@Override
	public void deleteStock(Long id, String username) {
		// TODO Auto-generated method stub
		stockRepository.deleteById(id);
	}

	@Override
	public Map<String, Object> getPortfolioMetrics(String username) throws UserDoesNotExistsException {
		// TODO Auto-generated method stub
		List<Stock> stocks;
		try {
			stocks = getAllStocks(username);
		} catch (UserDoesNotExistsException e) {
			// TODO Auto-generated catch block
			throw new UserDoesNotExistsException("No user with this credentials!!");
		} 
		double totalValue = 0; 
		String topStock = null; 
		double topStockValue = 0; 
		Map<String, Double> distribution = new HashMap<>(); 
		for (Stock stock : stocks) { 
			double currentPrice = getStockPrice(stock.getTicker()); 
			double stockValue = currentPrice * stock.getQuantity(); 
			totalValue += stockValue; 
			if (stockValue > topStockValue) { 
				topStockValue = stockValue; 
				topStock = stock.getTicker(); 
				} 
			distribution.put(stock.getTicker(), stockValue); 
			} 
		Map<String, Object> metrics = new HashMap<>(); 
		metrics.put("totalValue", totalValue); 
		metrics.put("topStock", topStock); 
		metrics.put("portfolioDistribution", distribution); 
		return metrics;
	}

	@Override
	public double getStockPrice(String ticker) {
		// TODO Auto-generated method stub
		String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("function", "TIME_SERIES_INTRADAY")
                .queryParam("symbol", ticker)
                .queryParam("interval", "1min")
                .queryParam("apikey", apiKey)
                .toUriString();

	    RestTemplate restTemplate = new RestTemplate();
	    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
	    
	    // Extract the latest price from the response
	    Map<String, Object> timeSeries = (Map<String, Object>) response.get("Time Series (1min)");
	    if (timeSeries == null) {
	      throw new RuntimeException("Failed to fetch data from Alpha Vantage API");
	    }
	
	    String latestTime = timeSeries.keySet().iterator().next();
	    Map<String, String> latestData = (Map<String, String>) timeSeries.get(latestTime);
	
	    return Double.parseDouble(latestData.get("1. open"));
	}

}
