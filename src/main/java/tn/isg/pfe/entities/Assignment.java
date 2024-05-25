package tn.isg.pfe.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(length = 100000)
    private String content;
    private String file_path;

    @Enumerated(EnumType.STRING)
    private State status = State.PENDING;
    @OneToOne
    @JsonIgnore
    private Training training;
    @ManyToOne
    @JsonIgnore
    @ToStringExclude
    private User user;
}
