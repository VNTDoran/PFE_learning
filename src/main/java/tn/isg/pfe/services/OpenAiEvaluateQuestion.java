package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiEvaluateQuestion {
    @Value("${api.key}")
    private static String apiKey;

    public static String execPromptGptForAnswer(String question, String answer) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        System.out.println("dezfezfezfr");
        MediaType mediaType = MediaType.parse("application/json");
        System.out.printf(question);
        String requestBodyJson = "{\n" +
                "    \"model\": \"gpt-3.5-turbo-16k\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"Evaluate the following answer: '" + answer + "' to the question: '" + question + "'. Provide a percentage indicating how valid the answer is. using json format example : {\\\"validity\\\": 95.0} no extra text nothing just the json object\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        RequestBody body = RequestBody.create(mediaType, requestBodyJson);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static double evaluateAnswer(String question, String answer) throws IOException {
        String jsonString = execPromptGptForAnswer(question, answer);
        Gson gson = new Gson();
        System.out.println(jsonString);
        JsonObject rootObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject messageObject = rootObject.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message");
        String contentString = messageObject.get("content").getAsString();
        return Double.parseDouble(contentString.replaceAll("[^0-9.]", ""));
    }
}
