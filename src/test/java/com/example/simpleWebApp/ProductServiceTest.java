package com.example.simpleWebApp;

import com.example.simpleWebApp.model.GenericResponse;
import com.example.simpleWebApp.enums.ErrorCode;
import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.repository.ProductRepository;
import com.example.simpleWebApp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        GenericResponse<Product> response = productService.createProduct(product);

        assertNotNull(response.getData());
        assertEquals("Test Product", response.getData().getName());
        assertNull(response.getErrorMessage());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        List<Product> productList = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(productList);

        GenericResponse<List<Product>> response = productService.getAllProducts();

        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals("Product 1", response.getData().get(0).getName());
        assertNull(response.getErrorMessage());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        GenericResponse<Product> response = productService.getProductById(1L);

        assertNotNull(response.getData());
        assertEquals("Test Product", response.getData().getName());
        assertNull(response.getErrorMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<Product> response = productService.getProductById(1L);

        assertNull(response.getData());
        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProduct() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");

        Product updatedProductDetails = new Product();
        updatedProductDetails.setName("Updated Product");
        updatedProductDetails.setDescription("Updated Description");
        updatedProductDetails.setPrice(200.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        GenericResponse<Product> response = productService.updateProduct(1L, updatedProductDetails);

        assertNotNull(response.getData());
        assertEquals("Updated Product", response.getData().getName());
        assertEquals(200.0, response.getData().getPrice());
        assertNull(response.getErrorMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        GenericResponse<Void> response = productService.deleteProduct(1L);

        assertNull(response.getErrorMessage());
        assertNull(response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        GenericResponse<Void> response = productService.deleteProduct(1L);

        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
    }


    @Test
    public void testCreateProduct_exception() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        GenericResponse<Product> response = productService.createProduct(product);

        assertNull(response.getData());
        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.PRODUCT_CREATION_FAILED, response.getErrorCode());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetAllProducts_exception() {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        GenericResponse<List<Product>> response = productService.getAllProducts();

        assertNull(response.getData());
        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, response.getErrorCode());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById_exception() {
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        GenericResponse<Product> response = productService.getProductById(1L);

        assertNull(response.getData());
        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProduct_exception() {
        Product updatedProductDetails = new Product();
        updatedProductDetails.setName("Updated Product");

        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        GenericResponse<Product> response = productService.updateProduct(1L, updatedProductDetails);

        assertNull(response.getData());
        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.PRODUCT_UPDATE_FAILED, response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteProduct_exception() {
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        GenericResponse<Void> response = productService.deleteProduct(1L);

        assertNotNull(response.getErrorMessage());
        assertEquals(ErrorCode.PRODUCT_DELETION_FAILED, response.getErrorCode());
        verify(productRepository, times(1)).findById(1L);
    }
}
