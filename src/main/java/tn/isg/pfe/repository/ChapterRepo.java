package tn.isg.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.isg.pfe.entities.Chapter;

import java.util.List;

@Repository
public interface ChapterRepo extends JpaRepository<Chapter,Long> {
}
