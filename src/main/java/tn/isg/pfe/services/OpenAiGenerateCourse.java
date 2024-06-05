package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiGenerateCourse {
    @Value("${api.key}")
    private static String apiKey;
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
                .addHeader("Authorization", "Bearer ")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String extractContent(String prompt) throws IOException {

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


}
