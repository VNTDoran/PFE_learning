package tn.isg.pfe.services;

import java.util.List;

import tn.isg.pfe.entities.Niveau;
import tn.isg.pfe.entities.Specialite;
import tn.isg.pfe.entities.CoursClassroom;


public interface ICoursClassroomService {

	List<CoursClassroom> retrieveAllCoursClassrooms();

	CoursClassroom ajouterCoursClassroom(CoursClassroom cc, Integer classeId);

	void deleteCoursClassroom(Integer id);

	CoursClassroom updateCoursClassroom(CoursClassroom cc);

	CoursClassroom retrieveCoursClassroom(Integer id);
	
	public void desaffecterCoursClassroomClasse(Integer idCours); 
		
	public void archiverCoursClassrooms(); 
	
	public Integer nbHeuresParSpecEtNiv(Specialite sp, Niveau nv);
	
}
