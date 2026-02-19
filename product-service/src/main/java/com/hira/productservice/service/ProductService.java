package com.hira.productservice.service;

import com.hira.productservice.dto.ProductRequestDTO;
import com.hira.productservice.dto.ProductResponseDTO;
import com.hira.productservice.entity.Product;
import com.hira.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO){
        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setQuantity(requestDTO.getQuantity());
        Product saved = productRepository.save(product);
        return mapToResponseDTO(saved);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setPrice(requestDTO.getPrice());
        existing.setQuantity(requestDTO.getQuantity());
        return mapToResponseDTO(productRepository.save(existing));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setQuantity(product.getQuantity());
        return responseDTO;
    }

}
