package com.tellimusveod.webapi.controller;

import com.tellimusveod.webapi.entity.OrderEntity;
import com.tellimusveod.webapi.model.MainData;
import com.tellimusveod.webapi.model.ResponseModel;
import com.tellimusveod.webapi.service.JobCategoryService;
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
    private OrderService jobService;

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobCategoryService jobCategoryService;

    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getAll() {
        List<OrderEntity> jobs = jobService.findAll();
        List<JobCategoryEntity> categories = jobCategoryService.findAll();
        HashMap<String, Object> combined = new HashMap<>();

        combined.put(KEY_JOBS, jobs);
        combined.put(KEY_CATEGORIES, categories);

        if (combined.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(combined, HttpStatus.OK);
    }


    @RequestMapping(value = "/start-work", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> apply(@RequestParam Integer applyerId) {
        jobService.applyToJob(applyerId);
        ResponseModel responseModel = new ResponseModel();
        responseModel.setMessage("You started work!");

        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/get-available-jobs", method = RequestMethod.GET)
    public ResponseEntity<?> getUserOffers(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Double distance, @RequestParam Integer userId) {
        List<OrderEntity> jobs = jobService.findAvailableJobsWithUserToken(latitude, longitude, distance, userId);
        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "/getalljobsbylocation", method = RequestMethod.GET)
    public ResponseEntity<?> getAllJobsByLocation(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Double distance) {
        List<OrderEntity> jobs = jobService.findAllNearestJobs(latitude, longitude, distance);

        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id") Integer id) {

        Optional<OrderEntity> job = jobService.findById(id);
        return ResponseEntity.ok(job);
    }

    @RequestMapping(value = "/getjobsbyaccount", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestParam Integer userId) {
        List<OrderEntity> jobs = jobService.findAllPostedJobs(userId);


        return ResponseEntity.ok(jobs);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody OrderEntity job) {
        jobService.save(job);
        return ResponseEntity.ok(job);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OrderEntity orderEntity) {

        if (jobService.exists(id)) {
            jobService.update(orderEntity);
            return ResponseEntity.ok(orderEntity);
        } else {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");
            return ResponseEntity.ok(responseModel);
        }
    }

    @RequestMapping(value = "registerjob/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> registerjob(@PathVariable("id") Integer id, @RequestBody OrderEntity orderEntity) {

        if (jobService.exists(id)) {
            jobService.update(orderEntity);

            return ResponseEntity.ok(orderEntity);
        } else {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");

            return ResponseEntity.ok(responseModel);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {

        if (!jobService.exists(id)) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");

            return ResponseEntity.ok(responseModel);
        }else {
            OrderEntity orderEntity = jobService.findSingleById(id);
            jobService.delete(id);


            return ResponseEntity.ok(orderEntity);
        }
    }

    @RequestMapping(value = "/get-my-upcoming-work", method = RequestMethod.GET)
    public ResponseEntity<?> getAppliedJobsByGooogleAccount(@RequestParam Integer userId) {
        List<OrderEntity> jobs = jobService.findUpcomingWork(userId);

        if (jobs.isEmpty()) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");
        }

        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "/getmydonework", method = RequestMethod.GET)
    public ResponseEntity<?> getMyDoneWork(@RequestParam Integer userId) {
        List<OrderEntity> jobs = jobService.findMyDoneWork(userId);

        if (jobs.isEmpty()) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("You have no jobs found!");
        }

        return ResponseEntity.ok(jobs);
    }

    @RequestMapping(value = "/get-main-data", method = RequestMethod.GET)
    public ResponseEntity<MainData> getMainData(@RequestParam Integer userId) {
        List<OrderEntity> applyedJobs = jobService.findUpcomingWork(userId);
        List<JobApplicationDTO> myCandidates = jobApplicationService.findCandidates(userId);

        MainData mainData = new MainData();
        mainData.setMyUpcomingWorkNumber(applyedJobs.size());
        mainData.setMyCandidatesNumber(myCandidates.size());

        return ResponseEntity.ok(mainData);
    }

    @RequestMapping(value = "deleteall", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteaLL() {
        jobService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}