package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import tn.isg.pfe.entities.Chapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiCheckResponse {

    private static String apiKey;

    @Value("${api.key}")
    public void setApiKey(String apiKey) {
        OpenAiCheckResponse.apiKey = apiKey;
    }

    public static String getApiKey() {
        return apiKey;
    }
    public static String execPromptGpt(String question , String answer , Chapter chapter) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        System.out.println(getApiKey());
        String requestBodyJson = "{\n" +
                "    \"model\": \"gpt-3.5-turbo-16k\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"I am giving you this question : ["+question+"] and this is the answer : ["+answer+"]. The answer of question should be related to the chapter's content : "+chapter+". your role is to check if the answer is true or close to the desire answer by giving a percentage for answer.The response should be in JSON format under this structure: {"+
                "} . The return response will be only JSON, no extra text.\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        System.out.println(requestBodyJson);
        String bearer = "Bearer " + "sk-proj-fX5hzQMljBqCrvtOrOZ6T3BlbkFJpMRsSvMv4KoEyOJZxzT4";
        RequestBody body = RequestBody.create(mediaType, requestBodyJson);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", bearer)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String extractContent(String question , String answer , Chapter chapter) throws IOException {

        // Execute the GPT prompt and get the JSON string response
        String jsonString = execPromptGpt(question, answer, chapter);
        Gson gson = new Gson();
        System.out.println("jsonString ////////////////////////////" + jsonString);

        // Access the "choices" array from the response and extract the "message" content
        JsonObject rootObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject messageObject = rootObject.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message");
        String contentString = messageObject.get("content").getAsString();

        return contentString;

    }
}