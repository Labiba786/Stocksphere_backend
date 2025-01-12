package com.personal.stocksphere.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.stocksphere.dto.StockDto;
import com.personal.stocksphere.entity.Stock;
import com.personal.stocksphere.exceptions.UserDoesNotExistsException;
import com.personal.stocksphere.service.StockService;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {
  @Autowired
  private StockService stockService;

  @PostMapping
  public ResponseEntity<?> addStock(@RequestBody StockDto stock) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    try {
		return new ResponseEntity<Stock>(stockService.addStock(stock, username), HttpStatus.CREATED);
	} catch (UserDoesNotExistsException e) {
		// TODO Auto-generated catch block
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}
  }

  @GetMapping
  public ResponseEntity<?> getAllStocks() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	String username = authentication.getName();
    try {
		return new ResponseEntity<List<Stock>>(stockService.getAllStocks(username), HttpStatus.OK);
	} catch (UserDoesNotExistsException e) {
		// TODO Auto-generated catch block
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}
  }
 
  @PutMapping("/{id}")
  public ResponseEntity<Stock> updateStock(@PathVariable("id") Long id, @RequestBody StockDto stock) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	String username = authentication.getName();
    return new ResponseEntity<Stock>(stockService.updateStock(id, stock, username), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStock(@PathVariable("id") Long id) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	String username = authentication.getName();
    stockService.deleteStock(id, username);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/metrics")
  public ResponseEntity<?> getPortfolioMetrics() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	String username = authentication.getName();
    try {
		return new ResponseEntity<>(stockService.getPortfolioMetrics(username), HttpStatus.OK);
	} catch (UserDoesNotExistsException e) {
		// TODO Auto-generated catch block
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
	}
  }
}
