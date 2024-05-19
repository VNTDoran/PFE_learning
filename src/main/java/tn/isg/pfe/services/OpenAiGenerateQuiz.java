package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
                "            \"content\": \""+prompt+"\"\n" +
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

    public static List<String> extractContent(String prompt) throws IOException {
        List<String> extractedContent = new ArrayList<>();

        String jsonString = execPromptGpt(prompt);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        for (JsonElement element : jsonArray) {
            JsonObject questionObject = element.getAsJsonObject();
            String question = questionObject.get("question").getAsString();
            JsonArray choices = questionObject.getAsJsonArray("choices");
            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append(question).append("\n");
            for (JsonElement choiceElement : choices) {
                JsonObject choiceObject = choiceElement.getAsJsonObject();
                String answer = choiceObject.get("answer").getAsString();
                boolean status = choiceObject.get("status").getAsBoolean();
                contentBuilder.append("- ").append(answer);
                if (status) {
                    contentBuilder.append(" (Correct)");
                }
                contentBuilder.append("\n");
            }
            extractedContent.add(contentBuilder.toString());
        }

        return extractedContent;
    }
}
