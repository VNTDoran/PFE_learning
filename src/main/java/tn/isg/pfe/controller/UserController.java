package tn.isg.pfe.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.isg.pfe.entities.MessageResponse;
import tn.isg.pfe.entities.User;
import tn.isg.pfe.entities.UserDTO;
import tn.isg.pfe.repository.UserRepo;

@RestController

public class UserController {

    @Autowired
    UserRepo userRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDTO loginRequest) {
        System.out.println(loginRequest.getEmail()+" "+loginRequest.getPassword());
        for (User user : userRepo.findAll()){
            System.out.println(user.getEmail().equals(loginRequest.getEmail()) && user.getPassword().equals(loginRequest.getPassword()));
            if (user.getEmail().equals(loginRequest.getEmail()) && user.getPassword().equals(loginRequest.getPassword())){
                return ResponseEntity.ok().body(new MessageResponse("ok"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("bad"));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO signUpRequest) {
        for (User user : userRepo.findAll()){
            if (user.getEmail().equals(signUpRequest.getEmail()) && user.getPassword().equals(signUpRequest.getPassword())){
                return ResponseEntity.badRequest().body(new MessageResponse("bad"));
            }
        }
        User usr = new User();
        usr.setPassword(signUpRequest.getPassword());
        usr.setEmail(signUpRequest.getEmail());
        userRepo.save(usr);
        return ResponseEntity.ok(new MessageResponse("ok"));
    }


}
