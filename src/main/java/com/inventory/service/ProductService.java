package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Check if product with same name already exists
        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new RuntimeException("Product with name '" + product.getName() + "' already exists");
        }
        
        // Check if SKU is provided and already exists
        if (product.getSku() != null && !product.getSku().isEmpty()) {
            if (productRepository.findBySku(product.getSku()).isPresent()) {
                throw new RuntimeException("Product with SKU '" + product.getSku() + "' already exists");
            }
        }
        
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!product.getName().equals(productDetails.getName())) {
            if (productRepository.findByName(productDetails.getName()).isPresent()) {
                throw new RuntimeException("Product with name '" + productDetails.getName() + "' already exists");
            }
        }

        // Check if SKU is being changed and if new SKU already exists
        if (productDetails.getSku() != null && !productDetails.getSku().isEmpty()) {
            if (!productDetails.getSku().equals(product.getSku())) {
                if (productRepository.findBySku(productDetails.getSku()).isPresent()) {
                    throw new RuntimeException("Product with SKU '" + productDetails.getSku() + "' already exists");
                }
            }
        }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setSku(productDetails.getSku());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product updateProductQuantity(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }
        
        product.setQuantity(quantity);
        return productRepository.save(product);
    }
}

