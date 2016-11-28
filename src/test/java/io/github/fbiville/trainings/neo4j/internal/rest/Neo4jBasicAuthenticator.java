package io.github.fbiville.trainings.neo4j.internal.rest;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Neo4jBasicAuthenticator implements Authenticator {

    private final String hash;

    public Neo4jBasicAuthenticator() {
        Properties credentials = loadCredentials();
        this.hash = hash(credentials.getProperty("username"), credentials.getProperty("password"));
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        return response.request()
                       .newBuilder()
                       .header("Authorization", hash)
                       .build();
    }

    private static Properties loadCredentials() {
        try (InputStream stream = Neo4jBasicAuthenticator.class.getResourceAsStream("/credentials.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String hash(String username, String password) {
        return Credentials.basic(username, password);
    }
}
