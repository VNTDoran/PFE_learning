package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Chapter;

@Repository
public interface ChapterRepo extends JpaRepository<Chapter,Long> {
}
