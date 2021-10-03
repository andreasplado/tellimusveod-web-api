package com.tellimusveod.webapi.service;

import com.tellimusveod.webapi.entity.OrderEntity;
import com.tellimusveod.webapi.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }


    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }


    @Override
    public OrderEntity update(OrderEntity orderEntity) {
        if(orderRepository.existsById(orderEntity.getId())){
            orderRepository.save(orderEntity);
        }
        return orderEntity;
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    @Override
    public Optional<OrderEntity> findById(Integer id) {
        return orderRepository.findById(id);
    }


    @Override
    public boolean exists(Integer id) {
        return orderRepository.existsById(id);
    }

}