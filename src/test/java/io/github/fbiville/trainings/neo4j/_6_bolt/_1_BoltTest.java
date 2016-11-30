package io.github.fbiville.trainings.neo4j._6_bolt;

import org.junit.Test;
import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class _1_BoltTest {

    @Test
    public void should_compute_the_average_salary_of_Doctor_Who_actors() {
        try (Driver driver = GraphDatabase.driver("bolt://localhost", credentials())) {
            Session session = driver.session(AccessMode.READ);
            StatementResult result = session.run(
                    "MATCH (a:Actor)-[:PLAYED]->(:Character {character:'Doctor'}) " +
                    "RETURN AVG(a.salary) AS cash");

            assertThat(result.single().get("cash").asInt()).isEqualTo(600000);
        }
    }

    private AuthToken credentials() {
        Properties properties = loadCredentials();
        return AuthTokens.basic(properties.getProperty("username"), properties.getProperty("password"));
    }

    private Properties loadCredentials() {
        try (InputStream credentials = this.getClass().getResourceAsStream("/credentials.properties")) {
            Properties properties = new Properties();
            properties.load(credentials);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
