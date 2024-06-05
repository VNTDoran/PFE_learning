package tn.isg.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Training {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String titre;
    @OneToMany
    List<Chapter> chapters;
    @OneToMany
    private List<Quiz> quizs=new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @OneToOne
    private Assignment assignment;

    @JsonIgnore
    @OneToMany
    private List<QNAQuestion> qnaQuestions=new ArrayList<>();


}
