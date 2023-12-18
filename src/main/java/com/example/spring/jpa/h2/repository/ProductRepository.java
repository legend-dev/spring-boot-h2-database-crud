package com.example.spring.jpa.h2.repository;

import java.util.List;

import com.example.spring.jpa.h2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByPublished(boolean published);

  List<Product> findByTitleContainingIgnoreCase(String title);
}
