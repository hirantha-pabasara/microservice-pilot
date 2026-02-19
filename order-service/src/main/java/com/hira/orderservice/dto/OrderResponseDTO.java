package com.hira.orderservice.dto;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private String status;
}