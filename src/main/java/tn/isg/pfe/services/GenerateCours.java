package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.stereotype.Service;
import tn.isg.pfe.entities.FormFormationGenerationDTO;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class GenerateCours {

    public static String execPromptGpt(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"model\": \"gpt-3.5-turbo-16k\",\r\n    \"messages\": [\r\n        {\r\n            \"role\": \"system\",\r\n            \"content\": \"" + prompt + "\"\r\n        }\r\n    ]\r\n}\r\n");
                Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer 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")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String extractContent() throws IOException {

        FormFormationGenerationDTO dto = new FormFormationGenerationDTO();
        dto.setTitre("CAP - Production et service en restaurations");
        dto.setCodeCrnp("RNCP4551");
        dto.setDomaine_de_formation("production and service in various types of catering, including fast food, collective catering, and cafeterias");
        dto.setCompetance_developpe("Food preparation, Inventory and supply management, Customer service, Production management, Teamwork, Adaptability, Knowledge of products and menus, Maintenance and cleaning");
        dto.setPublic_cible("students");

        String prompt = "I am creating an online training course to prepare candidates for obtaining the " + dto.getTitre() + " (training code: " + dto.getCodeCrnp() + "). This training aims to develop skills related to the professional qualification in " + dto.getDomaine_de_formation() + ", specifically in the areas of " + dto.getCompetance_developpe() + ". It targets an audience of " + dto.getPublic_cible() + " and will be delivered in the form of text. Now that I have all the information on the title " + dto.getCodeCrnp() + ", I need your help as an educational engineering expert to develop the structure of the course. A course is made up of several training courses, a training course includes several chapters, and each chapter is made up of several capsules, each pod aims to develop a specific skill through paragraphs and definitions and examples. I now want to generate content for each pod, including explanatory paragraphs, definitions, and relevant examples. Can you generate this content based on the structure as follows: 5 chapters, each chapter having 4 capsules without introduction and conclusion and generating the suggested content, the returned answer should be in a json format following the below example: { \\\"chapters\\\": [ { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] }, { \\\"chapter\\\": \\\"TITLE\\\", \\\"pods\\\": [ { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" }, { \\\"podtitle\\\": \\\"title\\\", \\\"content\\\": \\\"pod content\\\" } ] } ] } I only provided 2 examples in the json object but you need to always generate 5 chapters.";
        String jsonString = execPromptGpt(prompt);

        // Parse the JSON string
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // Extract the content field from choices array
        JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
        JsonObject firstChoice = choicesArray.get(0).getAsJsonObject();
        JsonObject message = firstChoice.getAsJsonObject("message");
        String content = message.get("content").getAsString();

        return content;
    }



   /* public static void main(String[] args) throws IOException {
        String response = null;
        try {
            response = GenerateCours.extractContent();

            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
