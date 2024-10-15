package com.example.simpleWebApp;

import com.example.simpleWebApp.controller.ProductController;
import com.example.simpleWebApp.model.GenericResponse;
import com.example.simpleWebApp.enums.ErrorCode;
import com.example.simpleWebApp.model.Product;
import com.example.simpleWebApp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createProductTest() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);

        GenericResponse<Product> response = new GenericResponse<>(product);

        when(productService.createProduct(any(Product.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andDo(print());

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    public void getAllProductsTest() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(200.0);

        List<Product> productList = Arrays.asList(product1, product2);
        GenericResponse<List<Product>> response = new GenericResponse<>(productList);

        when(productService.getAllProducts()).thenReturn(response);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Product 1"))
                .andExpect(jsonPath("$.data[1].name").value("Product 2"))
                .andDo(print());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void getProductByIdTest() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);

        GenericResponse<Product> response = new GenericResponse<>(product);

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andDo(print());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void getProductByIdNotFoundTest() throws Exception {
        GenericResponse<Product> response = new GenericResponse<>("Product not found", ErrorCode.PRODUCT_NOT_FOUND);

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Product not found"))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"))
                .andDo(print());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void updateProductTest() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(150.0);

        GenericResponse<Product> response = new GenericResponse<>(updatedProduct);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Product"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"))
                .andExpect(jsonPath("$.data.price").value(150.0))
                .andDo(print());


        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    public void deleteProductTest() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>(null);

        when(productService.deleteProduct(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andDo(print());

        verify(productService, times(1)).deleteProduct(1L);
    }


    @Test
    public void deleteProductNotFoundTest() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>("Product not found", ErrorCode.PRODUCT_NOT_FOUND);

        when(productService.deleteProduct(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Product not found"))
                .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"))
                .andDo(print());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
