package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Training;

import java.util.List;

@Repository
public interface TrainingRepo extends JpaRepository<Training,Long> {
}
