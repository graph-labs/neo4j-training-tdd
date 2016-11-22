package io.github.fbiville.trainings.neo4j.internal;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DoctorWhoGraph {

    public static void create(GraphDatabaseService graphDb) {
        execute(graphDb);
    }

    private static void execute(GraphDatabaseService graphDb) {
        List<String> statements = readLines("/dr-who.cypher");
        statements.forEach((statement) -> {
            try (Transaction transaction = graphDb.beginTx()) {
                graphDb.execute(statement);
                transaction.success();
            }
        });

    }

    private static List<String> readLines(String resource) {
        try (InputStream file = DoctorWhoGraph.class.getResourceAsStream(resource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

            return parseStatements(reader);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static List<String> parseStatements(BufferedReader reader) {
        StringBuilder builder = new StringBuilder();
        List<String> result = new ArrayList<>();
        List<String> rawLines = reader.lines().filter(l -> !l.trim().isEmpty()).collect(toList());
        for (String line : rawLines) {
            if (line.equals(":begin")) {
                builder = new StringBuilder();
            } else if (line.equals(":commit")) {
                result.add(builder.toString());
            } else {
                builder.append(line);
                builder.append("\n");
            }
        }
        return result;
    }
}
