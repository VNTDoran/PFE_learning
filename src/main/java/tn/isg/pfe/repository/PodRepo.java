package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Pod;

@Repository
public interface PodRepo extends JpaRepository<Pod,Long> {
}
