package com.tellimusveod.webapi.controller;

import com.tellimusveod.webapi.dao.entity.SettingsEntity;
import com.tellimusveod.webapi.dao.entity.UserEntity;
import com.tellimusveod.webapi.data.Note;
import com.tellimusveod.webapi.model.ResponseModel;
import com.tellimusveod.webapi.service.SettingsService;
import com.tellimusveod.webapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingsService settingsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserEntity> userEntities = userService.findAll();

        if (userEntities != null && userEntities.isEmpty()) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("Users not found!");
            return ResponseEntity.ok(responseModel);
        }

        return ResponseEntity.ok(userEntities);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String userId){
        Note note = new Note();
        note.setMessage("You logged out successfully");
        new InMemoryTokenStore().findTokensByClientId(userId).clear();
        return ResponseEntity.ok(note);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserEntity userEntity) {
        if(!userService.existByEmail(userEntity.getEmail())){
            userService.save(userEntity);
        }

        userEntity = userService.findByEmail(userEntity.getEmail());

        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserEntity user)
    {
        user.setEmail(user.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setContact(user.getContact());

        if(!userService.existByEmail(user.getEmail())){
            userService.save(user);

            int userId = userService.findId(user.getEmail());
            SettingsEntity settingsEntity = new SettingsEntity();
            settingsEntity.setBiometric(false);
            settingsEntity.setCreatedAt(new Date());
            settingsEntity.setCurrency("euro");
            settingsEntity.setUserId(userId);
            settingsEntity.setRadius(0.0);
            settingsEntity.setViewByDefault("available");
            settingsEntity.setAskPermissionsBeforeDeletingAJob(true);
            settingsService.save(settingsEntity);
        }else{
            Note note = new Note();
            note.setMessage("User with this email already exists!");
            return ResponseEntity.ok(note);
        }
        user = userService.findByEmail(user.getEmail());

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UserEntity userEntity) {

        if (!userService.exists(id)) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("User already exists!");
            return ResponseEntity.ok(responseModel);
        }
        userService.update(userEntity);

        return ResponseEntity.ok(userEntity);
    }


    @RequestMapping(value = "/update-role", method = RequestMethod.PUT)
    public ResponseEntity<?> updateRole(@RequestParam String userRole, @RequestParam Integer id) {
        if (!userService.exists(id)) {
            return ResponseEntity.ok("");
        }

        userService.updateUserRole(userRole, id);

        return ResponseEntity.ok(userRole);
    }

    @RequestMapping(value = "/update-firebase-token", method = RequestMethod.POST)
    public ResponseEntity<?> updateUserFirebaseToken(@RequestParam String firebaseToken, @RequestParam Integer userId) {
        userService.updateUserFirebaseToken(firebaseToken, userId);

        return ResponseEntity.ok(firebaseToken);
    }

    @RequestMapping(value = "/get-offerer-firebase-token", method = RequestMethod.GET)
    public ResponseEntity<?> updateUserFirebaseToken(@RequestParam Integer offererId) {
        String token = userService.getUserFirebaseToken(offererId);

        Note note = new Note();
        note.setMessage(token);

        return ResponseEntity.ok(note);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        Optional<UserEntity> userEntity = userService.findById(id);

        if (!userEntity.isPresent()) {
            ResponseModel responseModel = new ResponseModel();
            responseModel.setMessage("User not found!");
            return ResponseEntity.ok(responseModel);
        }

        userService.delete(id);
        return ResponseEntity.ok(userEntity);
    }
    @RequestMapping(value = "/does-exists", method = RequestMethod.GET)
        public ResponseEntity<Boolean> getMyApplications(@RequestParam String email) {
        boolean userExists = userService.existByEmail(email);
        return ResponseEntity.ok(userExists);
    }

    @RequestMapping(value = "/member-role", method = RequestMethod.GET)
    public ResponseEntity<String> isMember(@RequestParam int userId) {
        String memberRole = userService.memberRole(userId);

        return ResponseEntity.ok(memberRole);
    }

    @RequestMapping(value = "/set-role", method = RequestMethod.GET)
    public ResponseEntity<String> setRole(@RequestParam int userId) {
        String memberRole = userService.memberRole(userId);

        return ResponseEntity.ok(memberRole);
    }
}
