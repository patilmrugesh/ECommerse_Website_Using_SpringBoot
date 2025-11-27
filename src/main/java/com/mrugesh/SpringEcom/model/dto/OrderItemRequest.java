package com.mrugesh.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {
}
