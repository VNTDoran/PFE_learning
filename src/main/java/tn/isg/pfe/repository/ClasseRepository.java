package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.isg.pfe.entities.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer> {


}
