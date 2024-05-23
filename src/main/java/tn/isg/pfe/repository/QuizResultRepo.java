package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isg.pfe.entities.QuizResult;

import java.util.List;

public interface QuizResultRepo extends JpaRepository<QuizResult, Long> {
}