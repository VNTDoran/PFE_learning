package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isg.pfe.entities.QNAQuestion;
import tn.isg.pfe.entities.Question;

public interface QNAQstRepo  extends JpaRepository<QNAQuestion, Long> {
}
