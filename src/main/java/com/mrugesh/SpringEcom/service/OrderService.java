package com.mrugesh.SpringEcom.service;

import com.mrugesh.SpringEcom.model.Order;
import com.mrugesh.SpringEcom.model.OrderItem;
import com.mrugesh.SpringEcom.model.Product;
import com.mrugesh.SpringEcom.model.dto.OrderItemRequest;
import com.mrugesh.SpringEcom.model.dto.OrderItemResponse;
import com.mrugesh.SpringEcom.model.dto.OrderRequest;
import com.mrugesh.SpringEcom.model.dto.OrderResponse;
import com.mrugesh.SpringEcom.repo.OrderRepo;
import com.mrugesh.SpringEcom.repo.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        String orderId = "ORD"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : orderRequest.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            // Prevent negative stock
            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            // Update stock
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }


        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();

        for(OrderItem item: order.getOrderItems()){
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
            orderItemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(savedOrder.getOrderId()
                ,savedOrder.getCustomerName()
                ,savedOrder.getEmail()
                ,savedOrder.getStatus()
                ,savedOrder.getOrderDate(),
                orderItemResponses);

        System.out.println("yeye");

        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for(Order order: orders){

            List<OrderItemResponse> orderItemResponses = new ArrayList<>();

            for(OrderItem item: order.getOrderItems()){
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );

                orderItemResponses.add(orderItemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    orderItemResponses
            );
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
