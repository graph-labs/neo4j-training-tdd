package io.github.fbiville.trainings.neo4j.internal.rest;

import com.google.gson.Gson;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Neo4jRestClient {

    private final OkHttpClient client;
    private final Gson gson;

    public Neo4jRestClient(Authenticator authenticator) {
        this.client = new OkHttpClient.Builder().authenticator(authenticator).build();
        this.gson = new Gson();
    }

    public Response get(String url) {
        Request request = newRequestBuilder(url).build();
        return execute(request);
    }

    public TransactionalResponse postCypher(String url, TransactionalRequestPayload payload) {
        Response response = post(url, json(payload));
        String responseBody = body(response);
        assertThat(response.isSuccessful())
            .withFailMessage("Expected successful HTTP response, got %d.\nBody:%s", response.code(), responseBody)
            .isTrue();
        return gson.fromJson(responseBody, TransactionalResponse.class);
    }

    public Response post(String url, RequestBody payload) {
        Request request = newRequestBuilder(url).post(payload).build();
        return execute(request);
    }

    private Response execute(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private RequestBody json(TransactionalRequestPayload payload) {
        return RequestBody.create(MediaType.parse("application/json"), gson.toJson(payload));
    }

    private static Request.Builder newRequestBuilder(String url) {
        return new Request.Builder()
            .header("Accept", "application/json; charset=UTF-8")
            .url(url);
    }

    private static String body(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private static RuntimeException propagate(IOException e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}
