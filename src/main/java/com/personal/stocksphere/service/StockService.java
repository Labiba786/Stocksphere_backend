package com.personal.stocksphere.service;

import java.util.List;
import java.util.Map;


import com.personal.stocksphere.entity.Stock;



public interface StockService {
	
	public Stock addStock(Stock stock);
	
	public List<Stock> getAllStocks();
	
	public Stock updateStock(Long id, Stock updatedStock);
	
	public void deleteStock(Long id);
	
	public Map<String, Object> getPortfolioMetrics();
	
	public double getStockPrice(String ticker);

}
