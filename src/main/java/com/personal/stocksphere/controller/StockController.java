package com.personal.stocksphere.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.stocksphere.entity.Stock;
import com.personal.stocksphere.service.StockService;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
  @Autowired
  private StockService stockService;

  @PostMapping
  public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
    return new ResponseEntity<>(stockService.addStock(stock), HttpStatus.CREATED);
  }

  @GetMapping
  public List<Stock> getAllStocks() {
    return stockService.getAllStocks();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Stock> updateStock(@PathVariable("id") Long id, @RequestBody Stock stock) {
    return new ResponseEntity<>(stockService.updateStock(id, stock), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStock(@PathVariable("id") Long id) {
    stockService.deleteStock(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/metrics")
  public ResponseEntity<Map<String, Object>> getPortfolioMetrics() {
    return new ResponseEntity<>(stockService.getPortfolioMetrics(), HttpStatus.OK);
  }
}
