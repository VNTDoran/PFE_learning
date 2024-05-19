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
    private String titre;
    private String codeCrnp;
    private String domaine_de_formation;
    private String competance_developpe;
    private String public_cible;
}
