package com.example.spring.jpa.h2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.spring.jpa.h2.model.Product;
import com.example.spring.jpa.h2.repository.ProductRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllproducts(@RequestParam(required = false) String title) {
    try {
      List<Product> products = new ArrayList<>();

      if (title == null)
        productRepository.findAll().forEach(products::add);
      else
        productRepository.findByTitleContainingIgnoreCase(title).forEach(products::add);

      if (products.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
    Optional<Product> productData = productRepository.findById(id);

    if (productData.isPresent()) {
      return new ResponseEntity<>(productData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/products")
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    try {
      Product _product = productRepository.save(new Product(product.getTitle(), product.getDescription(), false));
      return new ResponseEntity<>(_product, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
    Optional<Product> productData = productRepository.findById(id);

    if (productData.isPresent()) {
      Product _product = productData.get();
      _product.setTitle(product.getTitle());
      _product.setDescription(product.getDescription());
      _product.setPublished(product.isPublished());
      return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/products/{id}")
  public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
    try {
      productRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/products")
  public ResponseEntity<HttpStatus> deleteAllproducts() {
    try {
      productRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/products/published")
  public ResponseEntity<List<Product>> findByPublished() {
    try {
      List<Product> products = productRepository.findByPublished(true);

      if (products.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
