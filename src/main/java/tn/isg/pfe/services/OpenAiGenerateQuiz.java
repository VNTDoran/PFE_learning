package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import okhttp3.*;
import org.springframework.stereotype.Service;
import tn.isg.pfe.entities.Quiz;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiGenerateQuiz {

    public static String execPromptGpt(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        String requestBodyJson = "{\n" +
                "    \"model\": \"gpt-3.5-turbo-16k\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"I am creating a quiz for the chapter "+prompt+". The quiz should include 5 multiple-choice questions, each with 4 possible answers related to the chapter's content. Each question's correct answer should be included among the possible answers. The questions should be related to the content of the chapter, and the answers should be relevant to the topic. The response should be in JSON format under this structure: {\\\"questions\\\":[{\\\"question\\\":\\\"What is the capital of France?\\\",\\\"choices\\\":[{\\\"choice\\\":\\\"Paris\\\",\\\"isCorrect\\\":true},{\\\"choice\\\":\\\"Berlin\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"London\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Rome\\\",\\\"isCorrect\\\":false}]},{\\\"question\\\":\\\"Which planet is known as the Red Planet?\\\",\\\"choices\\\":[{\\\"choice\\\":\\\"Mars\\\",\\\"isCorrect\\\":true},{\\\"choice\\\":\\\"Jupiter\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Venus\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Mercury\\\",\\\"isCorrect\\\":false}]},{\\\"question\\\":\\\"Who wrote the play 'Hamlet'?\\\",\\\"choices\\\":[{\\\"choice\\\":\\\"William Shakespeare\\\",\\\"isCorrect\\\":true},{\\\"choice\\\":\\\"Jane Austen\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Charles Dickens\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Leo Tolstoy\\\",\\\"isCorrect\\\":false}]},{\\\"question\\\":\\\"Which of the following is not a primary color?\\\",\\\"choices\\\":[{\\\"choice\\\":\\\"Green\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Red\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Blue\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"Yellow\\\",\\\"isCorrect\\\":true}]},{\\\"question\\\":\\\"What is the chemical symbol for water?\\\",\\\"choices\\\":[{\\\"choice\\\":\\\"H2O\\\",\\\"isCorrect\\\":true},{\\\"choice\\\":\\\"CO2\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"O2\\\",\\\"isCorrect\\\":false},{\\\"choice\\\":\\\"CH4\\\",\\\"isCorrect\\\":false}]}]} . Can you generate this quiz for me? The return response will be only JSON, no extra text.\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        System.out.println(requestBodyJson);

        RequestBody body = RequestBody.create(mediaType, requestBodyJson);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer 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")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String extractContent(String prompt) throws IOException {
        List<String> extractedContent = new ArrayList<>();

        // Execute the GPT prompt and get the JSON string response
        String jsonString = execPromptGpt(prompt);
        Gson gson = new Gson();
        System.out.println("jsonString ////////////////////////////" + jsonString);

        // Access the "choices" array from the response and extract the "message" content
        JsonObject rootObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject messageObject = rootObject.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message");
        String contentString = messageObject.get("content").getAsString();
        System.out.println("contentString ////////////////////////////" + contentString);
        return contentString;

    }

   /* public static void main(String[] args) throws IOException {
        System.out.println("Starting...");
        System.out.println("Done.");
        System.out.println("******************************************");
        System.out.println(extractContent("Chapter(id=1, title=Chapter 1: Food preparation, pods=[Pod(id=1, title=Pod 1: Knife skills, content=Knife skills are essential in food preparation as they ensure precision and efficiency when handling ingredients. There are different types of knife cuts that a professional in the catering industry should master, such as dicing, julienne, and chiffonade. Dicing involves cutting ingredients into small cubes, while julienne refers to thin, stick-like cuts. Chiffonade, on the other hand, involves rolling up leafy greens tightly and slicing them thinly. It is important for a candidate to practice these knife skills to improve their speed and accuracy in the kitchen.), Pod(id=2, title=Pod 2: Cooking techniques, content=A variety of cooking techniques are used in food preparation to enhance flavors and textures. Some common techniques include sautéing, grilling, baking, and braising. Sautéing involves cooking food quickly in a small amount of oil over high heat. Grilling is a method that uses direct heat from a grill to cook food and impart a smoky flavor. Baking involves cooking food in an oven by surrounding it with hot air, while braising combines both dry and moist heat to cook meat slowly until tender. Candidates should familiarize themselves with these cooking techniques to ensure they can execute recipes effectively.), Pod(id=3, title=Pod 3: Recipe conversions, content=Recipe conversions are necessary when scaling up or down recipes to accommodate different quantities of food. This skill is essential in catering, as it ensures consistency in taste and portion sizes. To convert a recipe, candidates must understand basic kitchen math and measurement conversions. For example, if a recipe calls for 2 cups of flour and you need to double it, you would need 4 cups. It is important for candidates to be able to accurately perform recipe conversions to maintain efficiency in the kitchen.), Pod(id=4, title=Pod 4: Food presentation, content=Food presentation plays a crucial role in the catering industry, as it influences the overall dining experience. Candidates should understand the principles of plating and garnishing to create visually appealing dishes. Plating refers to arranging food on a plate in an aesthetically pleasing manner, considering factors such as color, texture, and balance. Garnishing involves adding decorative elements to enhance the presentation. For example, herbs, edible flowers, and sauce drizzles can be used to add color and flavor to a dish. Candidates should practice their food presentation skills to showcase their culinary creativity.)])"));
    }*/


}
