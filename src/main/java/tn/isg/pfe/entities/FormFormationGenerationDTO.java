package tn.isg.pfe.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormFormationGenerationDTO {
    private String title;
    private String crnp;
    private String domain;
    private String competencies;
    private String audience;
    private String username;
}
