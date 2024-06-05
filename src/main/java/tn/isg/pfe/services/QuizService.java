package tn.isg.pfe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import tn.isg.pfe.entities.QNAQuestion;
import tn.isg.pfe.entities.Quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    public Quiz parseQuiz(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Quiz quiz = mapper.readValue(json, Quiz.class);
        return quiz;
    }
    public List<QNAQuestion> parseQuestions(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray questionsArray = jsonObject.getAsJsonArray("questions");

        List<QNAQuestion> questions = new ArrayList<>();
        for (JsonElement questionElement : questionsArray) {
            JsonObject questionObject = questionElement.getAsJsonObject();
            String questionText = questionObject.get("question").getAsString();

            QNAQuestion question = new QNAQuestion();
            question.setContent(questionText);
            questions.add(question);
        }

        return questions;
    }

}
