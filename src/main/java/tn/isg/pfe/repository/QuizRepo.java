package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
}
