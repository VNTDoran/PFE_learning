package tn.isg.pfe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PostExchange;
import tn.isg.pfe.entities.Chapter;
import tn.isg.pfe.entities.RateAnswerRequest;
import tn.isg.pfe.repository.ChapterRepo;
import tn.isg.pfe.services.OpenAiCheckResponse;
import tn.isg.pfe.services.OpenAiGenerateQuestion;

import java.io.IOException;
import java.util.Optional;

@RestController
public class GenerateQuestion {
    @Autowired
    ChapterRepo chapterRepo;

    @PostMapping("/generateQuestion/{id}")
    public String generateQuestion(@PathVariable Long id) throws IOException {
        Optional<Chapter> chapter = chapterRepo.findById(id);
        System.out.println(chapter);
        return OpenAiGenerateQuestion.extractContent(chapter);
    }

    @PostMapping("/rateAnswer/{chapId}")
    public String rateAnswer(@RequestBody RateAnswerRequest request,@PathVariable Long chapId) throws IOException {
        String question = request.getQuestion();
        String answer = request.getAnswer();

        Chapter chapter = chapterRepo.findById(chapId).get();
        System.out.println(question);
        System.out.println(answer);

       return OpenAiCheckResponse.extractContent(question,answer,chapter);
    }
}
