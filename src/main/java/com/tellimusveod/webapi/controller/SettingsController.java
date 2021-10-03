package com.tellimusveod.webapi.controller;


import com.tellimusveod.webapi.entity.SettingsEntity;
import com.tellimusveod.webapi.entity.UserEntity;
import com.tellimusveod.webapi.model.ResponseModel;
import com.tellimusveod.webapi.model.UserSettings;
import com.tellimusveod.webapi.service.SettingsService;
import com.tellimusveod.webapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    SettingsService settingsService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<SettingsEntity> settings = settingsService.findAll();

        return ResponseEntity.ok(settings);
    }

    @RequestMapping(value = "/get-user-settings", method = RequestMethod.GET)
    public ResponseEntity<?> getSettings(@RequestParam Integer userId) {
        SettingsEntity settings = settingsService.getUserSettings(userId);
        UserEntity user = userService.findUserById(userId);
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(settings.getUserId());
        userSettings.setCreatedAt(settings.getCreatedAt());
        userSettings.setCurrency(settings.getCurrency());
        userSettings.setShowInformationOnStartup(settings.getShowInformationOnStartup());
        userSettings.setEmail(user.getEmail());
        userSettings.setFullname(user.getFullname());
        userSettings.setUpdatedAt(user.getUpdatedAt());
        userSettings.setContact(user.getContact());
        userSettings.setRadius(settings.getRadius());
        userSettings.setRole(user.getRole());
        userSettings.setBiometric(settings.isBiometric());

        return ResponseEntity.ok(userSettings);
    }

    @RequestMapping(value = "/set-initial-settings", method = RequestMethod.POST)
    public ResponseEntity<?> setInitialSettings(@RequestBody SettingsEntity settingsEntity) {
        ResponseModel responseModel = new ResponseModel();

        if (!settingsService.exists(settingsEntity.getUserId())) {
            settingsService.save(settingsEntity);

            return ResponseEntity.ok(settingsEntity);
        } else {
            responseModel.setMessage("These settings for user with id: " + settingsEntity.getUserId() + " already exist!");

            return ResponseEntity.ok(responseModel);
        }
    }


    @RequestMapping(value = "/update-radius", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRadius(@RequestParam Integer userId, @RequestParam Double radius) {
        ResponseModel responseModel = new ResponseModel();
        settingsService.updateRadius(userId, radius);
        if(userService.exists(userId)){
            responseModel.setMessage("You updated");
            responseModel.setValid(true);
            responseModel.setRadius(radius);
            return ResponseEntity.ok(responseModel);
        }else {
            responseModel.setMessage("You failed");
            responseModel.setValid(false);
            responseModel.setRadius(radius);
            return ResponseEntity.ok(responseModel);
        }
    }

    @RequestMapping(value = "/update-view-by-default", method = RequestMethod.PUT)
    public ResponseEntity<?> updateViewByDefault(@RequestParam Integer userId, @RequestParam String value) {
        ResponseModel responseModel = new ResponseModel();

        settingsService.updateViewByDefault(userId, value);
        responseModel.setMessage("You updated view by default!");

        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/update-ask-permissions-before-deleting-a-job", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAskPermissionsBeforeDeletingAJob(@RequestParam Integer userId, @RequestParam Boolean value) {
        ResponseModel responseModel = new ResponseModel();

        settingsService.updateAskPermissionBeforeDeletingAJob(userId, value);
        responseModel.setMessage("You updated view by default!");

        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/update-biometric", method = RequestMethod.PUT)
    public ResponseEntity<?> updateBiometric(@RequestParam Integer userId, @RequestParam Boolean value) {
        ResponseModel responseModel = new ResponseModel();

        settingsService.updateBiometric(userId, value);
        responseModel.setMessage("You updated your biometric!");

        return ResponseEntity.ok(responseModel);
    }

    @RequestMapping(value = "/update-show-information-on-startup", method = RequestMethod.PUT)
    public ResponseEntity<?> updateShowInformationOnStartup(@RequestParam Integer userId, @RequestParam Boolean value) {
        ResponseModel responseModel = new ResponseModel();

        settingsService.updateShowInformationOnStartup(userId, value);
        responseModel.setMessage("You updated view by default!");

        return ResponseEntity.ok(responseModel);
    }

    @Scheduled(cron = "0 1 1 * * ?")
    public void myScheduledMethod() {
        settingsService.removeAllPersonsWhoAreNotMemberAnyMore();
    }
}
