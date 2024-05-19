package tn.isg.pfe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.isg.pfe.repository.UserRepo;

@RestController

public class User {

    @Autowired
    UserRepo userRepo;

    @GetMapping



}
