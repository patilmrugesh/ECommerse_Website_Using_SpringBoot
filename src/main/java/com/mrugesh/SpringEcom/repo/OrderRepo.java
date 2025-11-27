package com.mrugesh.SpringEcom.repo;

import com.mrugesh.SpringEcom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Integer> {
    Optional<Order> findByOrderId(String orderId);

}
