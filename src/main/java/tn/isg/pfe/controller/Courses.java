package tn.isg.pfe.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.isg.pfe.entities.FormFormationGenerationDTO;
import tn.isg.pfe.repository.ChapterRepo;
import tn.isg.pfe.repository.PodRepo;
import tn.isg.pfe.repository.TrainingRepo;
import tn.isg.pfe.services.GenerateCours;

import java.io.IOException;

@RestController
public class Courses {
    @Autowired GenerateCours generateCours;
    @Autowired TrainingRepo trainingRepo;
    @Autowired ChapterRepo chapterRepo;
    @Autowired PodRepo podRepo;


    @GetMapping("/generateCourse")
    public void generateCourse() {
        try {
            String response = GenerateCours.extractContent();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(response).getAsJsonObject();

            JsonArray chaptersArray = jsonObject.getAsJsonArray("chapters");

            // Iterate over the chapters array
            for (JsonElement chapterElement : chaptersArray) {
                JsonObject chapterObject = chapterElement.getAsJsonObject();
                // Extract chapter details
                String chapterName = chapterObject.get("chapter").getAsString();
                System.out.println("Chapter: " + chapterName);

                // Extract and print pod details
                JsonArray podsArray = chapterObject.getAsJsonArray("pods");
                for (JsonElement podElement : podsArray) {
                    JsonObject podObject = podElement.getAsJsonObject();
                    String podTitle = podObject.get("podtitle").getAsString();
                    String podContent = podObject.get("content").getAsString();
                    System.out.println("Pod Title: " + podTitle);
                    System.out.println("Pod Content: " + podContent);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
