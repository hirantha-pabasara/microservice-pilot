package com.hira.orderservice.service;

import com.hira.orderservice.client.ProductClient;
import com.hira.orderservice.dto.OrderRequestDTO;
import com.hira.orderservice.dto.OrderResponseDTO;
import com.hira.orderservice.dto.ProductResponseDTO;
import com.hira.orderservice.entity.Order;
import com.hira.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO){
        ProductResponseDTO product = productClient.getProductById(requestDTO.getProductId());
        Double totalPrice = product.getPrice() * requestDTO.getQuantity();
        Order order = new Order();
        order.setProductId(requestDTO.getProductId());
        order.setQuantity(requestDTO.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus("PENDING");

        Order saved = orderRepository.save(order);
        return mapToResponseDTO(saved);
    }

    public List<OrderResponseDTO> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found for this id :: "+id));
        return mapToResponseDTO(order);
    }

    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO requestDTO) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        ProductResponseDTO product = productClient.getProductById(requestDTO.getProductId());
        existing.setProductId(requestDTO.getProductId());
        existing.setQuantity(requestDTO.getQuantity());
        existing.setTotalPrice(product.getPrice() * requestDTO.getQuantity());
        return mapToResponseDTO(orderRepository.save(existing));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setProductId(order.getProductId());
        responseDTO.setQuantity(order.getQuantity());
        responseDTO.setTotalPrice(order.getTotalPrice());
        responseDTO.setStatus(order.getStatus());
        return responseDTO;
    }

}
