package tn.isg.pfe.services;

import java.util.List;

import tn.isg.pfe.entities.Niveau;
import tn.isg.pfe.entities.Utilisateur;


public interface IUtilisateurService {

	List<Utilisateur> retrieveAllUtilisateurs();

	Utilisateur ajouterUtilisateur(Utilisateur u);

	void deleteUtilisateur(Integer id);

	Utilisateur updateUtilisateur(Utilisateur u);

	Utilisateur retrieveUtilisateur(Integer id);
	
	void affecterUtilisateurClasse(Integer idUtilisateur, Integer idClasse); 
	
	public Integer nbUtilisateursParNiveau(Niveau n); 
	
//	public void desaffecterUtilisateurCoursClassroom (Integer idUtilisateur, Integer idCoursClassroom); 
//	
//	public Set<CoursClassroom> chercherCoursParUtilisateur(Integer idUtilisateur); 


}
