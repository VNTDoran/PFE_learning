package tn.isg.pfe.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.isg.pfe.entities.*;
import tn.isg.pfe.repository.*;
import tn.isg.pfe.services.OpenAiGenerateCourse;
import tn.isg.pfe.services.OpenAiGenerateQuiz;
import tn.isg.pfe.services.QuizService;

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
    @Autowired
    QuizService quizService;
    @Autowired
    QuizRepo quizRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired QuestionRepo questionRepo;


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

    @GetMapping("/generateQuiz/trainig/{trainigId}")
    public void generateQuizByTrainig(@PathVariable Long trainigId) {
        Training training = trainingRepo.findById(trainigId).orElseThrow(() -> new RuntimeException("Training not found"));
        System.out.println("test ///////////////////////////// "+training.getChapters().size());
        for (int i =0;i<training.getChapters().size();i++){
            System.out.println(i+" ****//*/**/ "+training.getChapters().get(i).getId());
            generateQuiz(training.getChapters().get(i).getId(), trainigId);
        }
    }

    public void generateQuiz(Long chapId, Long trainigId) {
        Chapter chapter = chapterRepo.findById(chapId).orElseThrow(() -> new RuntimeException("Chapter not found"));
        String prompt = chapter.toString();
        try {
            String response = OpenAiGenerateQuiz.extractContent(prompt);
            System.out.println(response);
            System.out.println("Quiz parsed successfully \n" + quizService.parseQuiz(response));
            Quiz quiz = quizService.parseQuiz(response);

            Quiz quiz1 = new Quiz();
            for (Question question : quiz.getQuestions()) {
                Question question1 = new Question();
                question1.setQuestion(question.getQuestion());
                for (Answer answer : question.getChoices()) {
                    Answer answer1 = new Answer();
                    answer1.setChoice(answer.getChoice());
                    answer1.setStatus(answer.isStatus());
                    answerRepo.save(answer1);
                    question1.getChoices().add(answer1);
                }
                questionRepo.save(question1);
                quiz1.getQuestions().add(question1);
            }
            quizRepo.save(quiz1);
            Training training = trainingRepo.findById(trainigId).orElseThrow(() -> new RuntimeException("Training not found"));
            training.getQuizs().add(quiz1);
            trainingRepo.save(training);


           // return response;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @GetMapping("/getQuizs/idTraining/{id}")
    public ResponseEntity<List<Quiz>> getQuizs(@PathVariable Long id) {
        Training training = trainingRepo.findById(id).orElseThrow(() -> new RuntimeException("Training not found"));
        return ResponseEntity.ok(training.getQuizs());
    }

    }
