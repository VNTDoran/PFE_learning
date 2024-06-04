package tn.isg.pfe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.isg.pfe.entities.Role;
import tn.isg.pfe.entities.User;
import tn.isg.pfe.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersManipulationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
    }

    @PutMapping("/promote/{id}")
    public User promoteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.ROLE_ADMIN);
            return userRepo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    @PutMapping("/downgrade/{id}")
    public User downgradeUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(Role.ROLE_USER);
            return userRepo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
