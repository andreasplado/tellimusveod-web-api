package com.tellimusveod.webapi.service;

import com.tellimusveod.webapi.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface IOrderService {

    List<OrderEntity> findAll();
    OrderEntity save (OrderEntity orderEntity);
    OrderEntity update(OrderEntity orderEntity);
    void delete(Integer id);
    void deleteAll();
    Optional<OrderEntity> findById(Integer id);
    boolean exists(Integer id);

    void applyToOrder(Integer applyerId);

    List<OrderEntity> getAvailableOrders(Double latitude, Double longitude, Double distance, Integer userId);

    List<OrderEntity> getMyOrders(Integer userId);
}
