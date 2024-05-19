package tn.isg.pfe.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
import java.util.Optional;

@RestController
public class Courses {
    @Autowired
    OpenAiGenerateCourse generateCours;
    @Autowired TrainingRepo trainingRepo;
    @Autowired ChapterRepo chapterRepo;
    @Autowired PodRepo podRepo;
    @Autowired
    OpenAiGenerateQuiz OpenAiGenerateQuiz;


    @PostMapping("/generateCourse")
    public void generateCourse(@RequestBody FormFormationGenerationDTO courseRequest) {
        try {

            String prompt = "I am creating an online training course to prepare candidates for obtaining the " + courseRequest.getTitle() + " (training code: " + courseRequest.getCrnp() + "). This training aims to develop skills related to the professional qualification in " + courseRequest.getDomain() + ", specifically in the areas of " + courseRequest.getCompetencies() + ". It targets an audience of " + courseRequest.getAudience() + " and will be delivered in the form of text. Now that I have all the information on the title " + courseRequest.getCrnp() + ", I need your help as an educational engineering expert to develop the structure of the course. A course is made up of several training courses, a training course includes several chapters, and each chapter is made up of several capsules, each pod aims to develop a specific skill through paragraphs and definitions and examples. I now want to generate content for each pod, including explanatory paragraphs, definitions, and relevant examples. Can you generate this content based on the structure as follows: 5 chapters, each chapter having 4 capsules without introduction and conclusion and generating the suggested content, the returned answer should be in a json format following the below example: { \\\"chapters\\\": [ { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] }, { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] } ] } I only provided 2 examples in the json object but you need to always generate 5 chapters.";
            System.out.println(courseRequest);

            String response = OpenAiGenerateCourse.extractContent(prompt);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response).getAsJsonObject();

            JsonArray chaptersArray = jsonObject.getAsJsonArray("chapters");
            Training training = new Training();
            training.setTitre(courseRequest.getTitle());
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

    @GetMapping("/getAllTraining")
    public List<Training> getAllChapters() {
        return trainingRepo.findAll();
    }

    @GetMapping("/getTraining/{id}")
    public Optional<Training> getTraining(@PathVariable Long id) {
        return trainingRepo.findById(id);
    }

    @DeleteMapping("/deleteTraining/{id}")
    public void deleteTraining(@PathVariable Long id) {
         trainingRepo.deleteById(id);
    }

    @GetMapping ("/getChapters/idTrainig/{id}")
    public List<Chapter> getChapters(@PathVariable Long id) {
        Training training = trainingRepo.findById(id).orElseThrow(() -> new RuntimeException("Training not found"));
        return training.getChapters();
    }

    @GetMapping("/getPods/idChapter/{id}")
    public List<Pod> getPods(@PathVariable Long id) {
        Chapter chapter = chapterRepo.findById(id).orElseThrow(() -> new RuntimeException("Chapter not found"));
        return chapter.getPods();
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
            String prompt ="I am creating a quiz for the chapter titled 'Chapter 1: Food preparation', which includes several learning pods: Knife Skills, Cooking Techniques, Recipe Conversions, and Food Presentation. Each pod has detailed content describing essential aspects for professional catering, like various knife cuts (dicing, julienne, chiffonade), cooking methods (sautéing, grilling, baking, braising), the art of scaling recipes, and the principles of aesthetically plating food. The quiz should include 5 multiple-choice questions, each with 4 possible answers related to the chapter's content. Each question's correct answer should be included among the possible answers. The questions should be related to the content of the chapter, and the answers should be relevant to the topic. The response should be in JSON format under this structure: {\"question\": \"...\", \"choices\": [{\"answer\": \"...\", \"status\": \"true\"}]}. Can you generate this quiz for me? The return response will be only JSON, no extra text.";

            List<String> response = OpenAiGenerateQuiz.extractContent(prompt);
            for (String question : response) {
                System.out.println(question);
            }



        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    }
