package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isg.pfe.entities.Assignment;
import tn.isg.pfe.entities.State;

import java.util.List;

public interface AssignmentRepo extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByUser_Id(Long id);
    List<Assignment> findAllByStatus(State e);

}
