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
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return repo.save(product);
    }

    public Product updateProduct(int id, Product updatedProduct, MultipartFile imageFile) throws IOException {

        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update normal fields
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setBrand(updatedProduct.getBrand());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setReleaseDate(updatedProduct.getReleaseDate());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setProductAvailable(updatedProduct.getProductAvailable());

        // Update image ONLY if new image is uploaded
        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImageName(imageFile.getOriginalFilename());
            existing.setImageType(imageFile.getContentType());
            existing.setImageData(imageFile.getBytes());
        }

        return repo.save(existing);
    }


    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
