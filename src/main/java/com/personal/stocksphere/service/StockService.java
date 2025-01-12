package com.personal.stocksphere.service;

import java.util.List;
import java.util.Map;

import com.personal.stocksphere.dto.StockDto;
import com.personal.stocksphere.entity.Stock;
import com.personal.stocksphere.exceptions.UserDoesNotExistsException;



public interface StockService {
	
	public Stock addStock(StockDto stock, String username) throws UserDoesNotExistsException;
	
	public List<Stock> getAllStocks(String username) throws UserDoesNotExistsException;
	
	public Stock updateStock(Long id, StockDto updatedStock, String username);
	
	public void deleteStock(Long id, String username);
	
	public double getStockPrice(String ticker);

	Map<String, Object> getPortfolioMetrics(String username) throws UserDoesNotExistsException;

}
