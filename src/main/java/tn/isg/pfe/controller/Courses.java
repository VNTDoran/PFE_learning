package tn.isg.pfe.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tn.isg.pfe.entities.Chapter;
import tn.isg.pfe.entities.FormFormationGenerationDTO;
import tn.isg.pfe.entities.Pod;
import tn.isg.pfe.entities.Training;
import tn.isg.pfe.repository.ChapterRepo;
import tn.isg.pfe.repository.PodRepo;
import tn.isg.pfe.repository.TrainingRepo;
import tn.isg.pfe.services.OpenAiGenerateCourse;
import tn.isg.pfe.services.OpenAiGenerateQuiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Courses {
    @Autowired
    OpenAiGenerateCourse generateCours;
    @Autowired TrainingRepo trainingRepo;
    @Autowired ChapterRepo chapterRepo;
    @Autowired PodRepo podRepo;
    @Autowired
    OpenAiGenerateQuiz OpenAiGenerateQuiz;


    @GetMapping("/generateCourse")
    public void generateCourse() {
        try {
            FormFormationGenerationDTO dto = new FormFormationGenerationDTO();
            dto.setTitre("CAP - Production et service en restaurations");
            dto.setCodeCrnp("RNCP4551");
            dto.setDomaine_de_formation("production and service in various types of catering, including fast food, collective catering, and cafeterias");
            dto.setCompetance_developpe("Food preparation, Inventory and supply management, Customer service, Production management, Teamwork, Adaptability, Knowledge of products and menus, Maintenance and cleaning");
            dto.setPublic_cible("students");

            String prompt = "I am creating an online training course to prepare candidates for obtaining the " + dto.getTitre() + " (training code: " + dto.getCodeCrnp() + "). This training aims to develop skills related to the professional qualification in " + dto.getDomaine_de_formation() + ", specifically in the areas of " + dto.getCompetance_developpe() + ". It targets an audience of " + dto.getPublic_cible() + " and will be delivered in the form of text. Now that I have all the information on the title " + dto.getCodeCrnp() + ", I need your help as an educational engineering expert to develop the structure of the course. A course is made up of several training courses, a training course includes several chapters, and each chapter is made up of several capsules, each pod aims to develop a specific skill through paragraphs and definitions and examples. I now want to generate content for each pod, including explanatory paragraphs, definitions, and relevant examples. Can you generate this content based on the structure as follows: 5 chapters, each chapter having 4 capsules without introduction and conclusion and generating the suggested content, the returned answer should be in a json format following the below example: { \\\"chapters\\\": [ { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] }, { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] } ] } I only provided 2 examples in the json object but you need to always generate 5 chapters.";


            String response = OpenAiGenerateCourse.extractContent(prompt);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response).getAsJsonObject();

            JsonArray chaptersArray = jsonObject.getAsJsonArray("chapters");
            Training training = new Training();
            training.setTitre("CAP - Production et service en restaurations");
            ArrayList<Chapter> chapters = new ArrayList<>();

            // Iterate over the chapters array
            for (JsonElement chapterElement : chaptersArray) {
                JsonObject chapterObject = chapterElement.getAsJsonObject();
                // Extract chapter details
                String chapterName = chapterObject.get("chapter").getAsString();
                System.out.println("Chapter: " + chapterName);
                Chapter chapter = new Chapter();
                chapter.setTitle(chapterName);
                ArrayList<Pod> pods = new ArrayList<>();
                chapter.setPods(pods);


                // Extract and print pod details
                JsonArray podsArray = chapterObject.getAsJsonArray("pods");
                for (JsonElement podElement : podsArray) {
                    JsonObject podObject = podElement.getAsJsonObject();
                    String podTitle = podObject.get("podtitle").getAsString();
                    String podContent = podObject.get("content").getAsString();
                    System.out.println("Pod Title: " + podTitle);
                    System.out.println("Pod Content: " + podContent);
                    Pod pod = new Pod();
                    pod.setTitle(podTitle);
                    pod.setContent(podContent);
                    podRepo.save(pod);
                    pods.add(pod);
                }
                chapterRepo.save(chapter);
                chapters.add(chapter);
            }
            training.setChapters(chapters);
            trainingRepo.save(training);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/getChapter/{id}")
    public String getChapter(@PathVariable Long id) {
        return chapterRepo.findById(id)
                .map(Object::toString)
                .orElse("Chapter not found");
    }

    @GetMapping("/generateQuiz/chapId/{chapId}")
    public void generateQuiz(@PathVariable Long chapId) {
        try {
            Chapter chapter = chapterRepo.findById(chapId).orElseThrow(() -> new RuntimeException("Chapter not found"));
            System.out.println("Chapter: " + chapter.toString());
            String prompt = "I am creating a quiz for the chapter \" \n" +
                    "\n" +
                    chapter.toString()+
                    "\n" +
                    " \". The quiz should include 5 multiple-choice questions, each with 4 possible answers. The correct answer should be included in the list of possible answers. The questions should be related to the content of the chapter, and the answers should be relevant to the topic. \n" +
                    "\n" +
                    "the response should be in json format under this stucture : \n" +
                    "{\n" +
                    "\"question\":\"....\",\n" +
                    "\"choises\":[\n" +
                    "{\n" +
                    "\"answer\":\"....\",\n" +
                    "\"status\":\"true\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "\n" +
                    "Can you generate this quiz for me?\n" +
                    "\n" +
                    "only the json format";

            List<String> response = OpenAiGenerateQuiz.extractContent(prompt);
            for (String question : response) {
                System.out.println(question);
            }



        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    }
