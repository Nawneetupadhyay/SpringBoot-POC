package com.example.simpleWebApp.service;

import com.example.simpleWebApp.model.GenericResponse;
import com.example.simpleWebApp.enums.ErrorCode;
import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public GenericResponse<Product> createProduct(Product product) {
        try {
            Product savedProduct = productRepository.save(product);
            return new GenericResponse<>(savedProduct);
        } catch (Exception e) {
            return new GenericResponse<>("Error creating product", ErrorCode.PRODUCT_CREATION_FAILED);
        }
    }

    public GenericResponse<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new GenericResponse<>(products);
        } catch (Exception e) {
            return new GenericResponse<>("Error fetching products", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public GenericResponse<Product> getProductById(Long id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                return new GenericResponse<>(product.get());
            } else {
                return new GenericResponse<>("Product not found", ErrorCode.PRODUCT_NOT_FOUND);
            }
        } catch (Exception e) {
            return new GenericResponse<>("Error fetching product by id", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public GenericResponse<Product> updateProduct(Long id, Product productDetails) {
        try {
            Optional<Product> existingProduct = productRepository.findById(id);
            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                product.setName(productDetails.getName());
                product.setPrice(productDetails.getPrice());
                // Other fields update logic
                return new GenericResponse<>(productRepository.save(product));
            } else {
                return new GenericResponse<>("Product not found", ErrorCode.PRODUCT_NOT_FOUND);
            }
        } catch (Exception e) {
            return new GenericResponse<>("Error updating product", ErrorCode.PRODUCT_UPDATE_FAILED);
        }
    }

    public GenericResponse<Void> deleteProduct(Long id) {
        try {
            Optional<Product> existingProduct = productRepository.findById(id);
            if (existingProduct.isPresent()) {
                productRepository.delete(existingProduct.get());
                return new GenericResponse<>(null);
            } else {
                return new GenericResponse<>("Product not found", ErrorCode.PRODUCT_NOT_FOUND);
            }
        } catch (Exception e) {
            return new GenericResponse<>("Error deleting product", ErrorCode.PRODUCT_DELETION_FAILED);
        }
    }
}
