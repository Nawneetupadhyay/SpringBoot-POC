package com.example.simpleWebApp.controller;

import com.example.simpleWebApp.model.GenericResponse;
import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create Product
    @PostMapping
    public ResponseEntity<GenericResponse<Product>> createProduct(@RequestBody Product product) {
        GenericResponse<Product> response = productService.createProduct(product);
        return buildResponse(response);
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<GenericResponse<List<Product>>> getAllProducts() {
        GenericResponse<List<Product>> response = productService.getAllProducts();
        return buildResponse(response);
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<Product>> getProductById(@PathVariable Long id) {
        GenericResponse<Product> response = productService.getProductById(id);
        return buildResponse(response);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        GenericResponse<Product> response = productService.updateProduct(id, productDetails);
        return buildResponse(response);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteProduct(@PathVariable Long id) {
        GenericResponse<Void> response = productService.deleteProduct(id);
        return buildResponse(response);
    }

    // Utility method to build the response based on ApiResponse
    private <T> ResponseEntity<GenericResponse<T>> buildResponse(GenericResponse<T> response) {
        if (response.getErrorMessage() == null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getErrorCode().getHttpStatus()).body(response);
        }
    }
}
