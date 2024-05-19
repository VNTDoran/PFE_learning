package tn.isg.pfe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tn.isg.pfe.entities.Quiz;

import java.io.IOException;
@Service
public class QuizService {
    public Quiz parseQuiz(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Quiz quiz = mapper.readValue(json, Quiz.class);
        return quiz;
    }
}
