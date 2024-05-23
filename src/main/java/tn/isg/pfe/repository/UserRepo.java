package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isg.pfe.entities.User;

public interface UserRepo extends JpaRepository<User,Long> {
    User findUserByEmail(String email);
}
