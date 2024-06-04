package tn.isg.pfe.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAiCheckResponse {
    @Value("${api.key}")
    private static String apiKey;
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
                "            \"content\": \"I am creating an assignment for the combined chapters : "+prompt+". The assignment should be a paragraph related to the chapter's content. The response should be in JSON format under this structure: {"+
                "\\\"assignment\\\": \\\"make an essay where you summarize what you learned\\\" "+
                "} . Can you generate this assignment for me? The return response will be only JSON, no extra text.\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        System.out.println(requestBodyJson);

        RequestBody body = RequestBody.create(mediaType, requestBodyJson);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+apiKey)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String extractContent(String prompt) throws IOException {

        // Execute the GPT prompt and get the JSON string response
        String jsonString = execPromptGpt(prompt);
        Gson gson = new Gson();
        System.out.println("jsonString ////////////////////////////" + jsonString);

        // Access the "choices" array from the response and extract the "message" content
        JsonObject rootObject = gson.fromJson(jsonString, JsonObject.class);
        JsonObject messageObject = rootObject.getAsJsonArray("choices").get(0).getAsJsonObject().getAsJsonObject("message");
        String contentString = messageObject.get("content").getAsString();
        JsonObject contentJson = JsonParser.parseString(contentString).getAsJsonObject();
        String assignmentValue = contentJson.get("assignment").getAsString();

        System.out.println("++++++++++++++++"+assignmentValue);
        return assignmentValue;

    }

}
