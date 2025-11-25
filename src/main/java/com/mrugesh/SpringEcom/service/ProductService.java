package com.mrugesh.SpringEcom.service;

import com.mrugesh.SpringEcom.model.Product;
import com.mrugesh.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    public ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }


    public Product getProductById(int id) {
        return repo.findById(id).orElse(new Product());
    }
}
