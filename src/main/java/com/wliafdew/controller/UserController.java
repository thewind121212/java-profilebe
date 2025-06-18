package com.wliafdew.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wliafdew.crypto.JwtTokenProvider;
import com.wliafdew.domain.user.UsersServiceImpl;
import com.wliafdew.exception.ResourceNotFoundException;
import com.wliafdew.model.users;
import com.wliafdew.service.MailService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UsersServiceImpl usersServiceImpl;
    MailService mailService;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setUsersServiceImpl(UsersServiceImpl usersServiceImpl) {
        this.usersServiceImpl = usersServiceImpl;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public List<users> getAllUsers() {
        return usersServiceImpl.getUsers();
    }

    @PostMapping("/registeruser")
    public ResponseEntity<users> registerUser(@RequestBody RequestBodyCreateUser requestBodyCreateUser) throws Exception {
        users user = new users();
        user.setEmail(requestBodyCreateUser.getEmail());
        user.setPassword(requestBodyCreateUser.getPassword());
        user.setFristname("");
        user.setLastname("");
        user.setAvatar("");
        user.setCreatedat(new Date());
        user.setUpdatedat(new Date());
        user.setIsactive(false);//-> account is not active for fist time (need confirm mail)
        user = usersServiceImpl.insertUser(user);//-> insert user -> id has been created

        //send mail
        //todo checking when send email
        this.mailService.sendVerificationEmail(requestBodyCreateUser.getEmail(), user.getId());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/verifyEmailToken")
    public ResponseEntity<Object> verifyEmailToken(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        
        //check token
        if (token == null && !jwtTokenProvider.validateToken(token)) 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        //get email
        String emailString = jwtTokenProvider.extractEmail(token);
        //get uuid
        UUID uuid = UUID.fromString(jwtTokenProvider.extractId(token));
        //get user
        users user = usersServiceImpl.getUserByID(uuid);
        //check email confirm
        if(!emailString.equals(user.getEmail()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        //set is active true
        user.setIsactive(true);

        //save
        usersServiceImpl.updateUsers(user);

        return ResponseEntity.ok().build();
    }


    @PutMapping
    public ResponseEntity<users> updateUser(@RequestBody users user) throws ResourceNotFoundException {
        return ResponseEntity.ok(usersServiceImpl.updateUsers(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) throws ResourceNotFoundException {
        usersServiceImpl.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<users> getUserById(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(usersServiceImpl.getUserByID(id));
    }

    public static class RequestBodyCreateUser{
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
