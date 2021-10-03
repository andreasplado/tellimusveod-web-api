package com.tellimusveod.webapi.controller;

import com.tellimusveod.webapi.entity.OrderEntity;
import com.tellimusveod.webapi.model.MainData;
import com.tellimusveod.webapi.model.ResponseModel;
import com.tellimusveod.webapi.service.OrderService;
import com.tellimusveod.webapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private static String KEY_JOBS = "jobs";
    private static String KEY_CATEGORIES = "categories";
    private static String KEY_FIREBASE_TOKEN = "firebasetoken";

    @Autowired
    private UserService userservice;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getAll() {
        List<OrderEntity> jobs = orderService.findAll();

        HashMap<String, Object> combined = new HashMap<>();

        combined.put(KEY_JOBS, jobs);

        if (combined.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(combined, HttpStatus.OK);
    }


    @RequestMapping(value = "/start-work", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> apply(@RequestParam Integer applyerId) {
        orderService.applyToOrder(applyerId);
        ResponseModel responseModel = new ResponseModel();
        responseModel.setMessage("You started work!");

        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/get-available-orders", method = RequestMethod.GET)
    public ResponseEntity<?> getUserOffers(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Double distance, @RequestParam Integer userId) {
        List<OrderEntity> jobs = orderService.getAvailableOrders(latitude, longitude, distance, userId);
        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {

        Optional<OrderEntity> job = orderService.findById(id);
        return ResponseEntity.ok(job);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody OrderEntity job) {
        orderService.save(job);
        return ResponseEntity.ok(job);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OrderEntity orderEntity) {

        if (orderService.exists(id)) {
            orderService.update(orderEntity);
            return ResponseEntity.ok(orderEntity);
        } else {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");
            return ResponseEntity.ok(responseModel);
        }
    }

    @RequestMapping(value = "registerjob/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> registerjob(@PathVariable("id") Integer id, @RequestBody OrderEntity orderEntity) {

        if (orderService.exists(id)) {
            orderService.update(orderEntity);

            return ResponseEntity.ok(orderEntity);
        } else {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");

            return ResponseEntity.ok(responseModel);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {

        if (!orderService.exists(id)) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no orders found!");

            return ResponseEntity.ok(responseModel);
        }else {
            Optional<OrderEntity> orderEntity = orderService.findById(id);
            orderService.delete(id);


            return ResponseEntity.ok(orderEntity);
        }
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<?> getMyDoneWork(@RequestParam Integer userId) {
        List<OrderEntity> orders = orderService.getMyOrders(userId);

        if (orders.isEmpty()) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no orders!");
        }

        return ResponseEntity.ok(orders);
    }

    @RequestMapping(value = "/get-main-data", method = RequestMethod.GET)
    public ResponseEntity<MainData> getMainData(@RequestParam Integer userId) {
        MainData mainData = new MainData();

        return ResponseEntity.ok(mainData);
    }

    @RequestMapping(value = "deleteall", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteaLL() {
        orderService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}