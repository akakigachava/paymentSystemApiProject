package com.example.withdb.dao;

import com.example.withdb.entity.Product;

import java.util.List;
import java.util.Optional;

public interface BaseProductDAO {
    void save(Product product);
    Product findById(int id);
    List<Product> findAll();
    void update(Product product);
    boolean delete(String productName);
    Optional<Product> findByName(String name);
}
