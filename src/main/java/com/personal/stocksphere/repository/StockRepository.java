package com.personal.stocksphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.stocksphere.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
