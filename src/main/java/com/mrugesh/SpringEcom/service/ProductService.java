package com.mrugesh.SpringEcom.service;

import com.mrugesh.SpringEcom.model.Product;
import com.mrugesh.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return repo.save(product);
    }

    public Product updateProduct(int id, Product updatedProduct, MultipartFile imageFile) throws IOException {
        updatedProduct.setImageName(imageFile.getOriginalFilename());
        updatedProduct.setImageType(imageFile.getContentType());
        updatedProduct.setImageData(imageFile.getBytes());
        return repo.save(updatedProduct);
    }

    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
