package tn.isg.pfe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotBlank
    @Size(max = 120)
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Training> trainingSet;

    @OneToMany(cascade = CascadeType.ALL)
    private List<QuizResult> quizResults;
}
