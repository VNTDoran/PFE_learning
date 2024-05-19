package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Question;
@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
}
