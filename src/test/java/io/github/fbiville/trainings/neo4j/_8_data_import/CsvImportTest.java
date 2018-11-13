package io.github.fbiville.trainings.neo4j._8_data_import;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.rule.DatabaseRule;
import org.neo4j.test.rule.ImpermanentDatabaseRule;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

public class CsvImportTest {

    @Rule
    public WireMockRule csvServer = new WireMockRule(options().dynamicPort());

    @Rule
    public DatabaseRule graphDb = new ImpermanentDatabaseRule();

    @Test
    public void imports_word_graph_from_CSV() {
        String uri = serveCsv("example.csv", "hello,world\nbonjour,monde");
        var cql = "LOAD CSV FROM '" + uri + "' AS row CREATE (:Word {word: row[0]})-[:NEXT]->(:Word {word: row[1]})";

        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            var validationQuery = "MATCH (w1:Word)-[:NEXT]->(w2:Word) " +
                    "RETURN w1.word AS word1, w2.word AS word2";
            Result result = graphDb.execute(validationQuery);

            assertThat(result)
                    .containsExactly(
                            Map.of("word1", "hello", "word2", "world"),
                            Map.of("word1", "bonjour", "word2", "monde")
                    );
        }
    }

    @Test
    public void imports_word_graph_from_CSV_with_headers() {
        String uri = serveCsv("example.csv", "word1,word2\nhello,world\nbonjour,monde");
        var cql = "LOAD CSV WITH HEADERS FROM '" + uri + "' AS row CREATE (:Word {word: row.word1})-[:NEXT]->(:Word {word: row.word2})";

        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            var validationQuery = "MATCH (w1:Word)-[:NEXT]->(w2:Word) " +
                    "RETURN w1.word AS word1, w2.word AS word2";
            Result result = graphDb.execute(validationQuery);

            assertThat(result)
                    .containsExactly(
                            Map.of("word1", "hello", "word2", "world"),
                            Map.of("word1", "bonjour", "word2", "monde")
                    );
        }
    }

    @Test
    public void imports_word_graph_from_CSV_with_headers_and_custom_delimiter() {
        String uri = serveCsv("example.csv", "word1$word2\nhello$world\nbonjour$monde");
        var cql = "LOAD CSV WITH HEADERS FROM '" + uri + "' AS row " +
                "FIELDTERMINATOR '$'" +
                "CREATE (:Word {word: row.word1})-[:NEXT]->(:Word {word: row.word2})";

        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            var validationQuery = "MATCH (w1:Word)-[:NEXT]->(w2:Word) " +
                    "RETURN w1.word AS word1, w2.word AS word2";
            Result result = graphDb.execute(validationQuery);

            assertThat(result)
                    .containsExactly(
                            Map.of("word1", "hello", "word2", "world"),
                            Map.of("word1", "bonjour", "word2", "monde")
                    );
        }
    }

    private String serveCsv(String filename, String csv) {
        csvServer.stubFor(get(urlEqualTo("/" + filename))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/csv")
                        .withStatus(200)
                        .withBody(csv)));
        return String.format("http://localhost:%d/example.csv", csvServer.port());
    }
}
