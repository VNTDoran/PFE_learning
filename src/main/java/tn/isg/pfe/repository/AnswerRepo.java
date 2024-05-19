package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Answer;
@Repository
public interface AnswerRepo extends JpaRepository<Answer, Long> {
}
