package io.github.fbiville.trainings.neo4j.internal;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DoctorWhoGraph {

    private static final Pattern NODE_CREATION =        Pattern.compile("^CREATE \\(_(\\d+)(\\:.*)\\);?");
    private static final Pattern RELATION_CREATION =    Pattern.compile("^CREATE \\(_(\\d+)\\)-(.*)->\\(_(\\d+)\\);?");
    private static final int PARTITION_SIZE =           100;

    public static void create(GraphDatabaseService graphDb) {
        execute(graphDb);
    }

    private static void execute(GraphDatabaseService graphDb) {
        List<String> standaloneStatements = toStandaloneStatements(readLines("/dr-who.cypher"));
        CollectionOperations.forEachOrderedPartition(
                PARTITION_SIZE,
                standaloneStatements,
                batch -> executeBatch(graphDb, batch)
        );
    }

    /**
     * Transforms dependent expressions into standalone statements.
     * Turns for instance this multiline-statement:
     * 
     * CREATE (_1:A)
     * CREATE (_2:B)
     * CREATE (_1)-[:C]->(_2);
     * 
     * into
     * 
     * MERGE (start:A) MERGE (end:B) CREATE (start)-[:C]->(end)
     * 
     * Assumptions:
     *  - node variable names start with an underscore, followed by a natural
     *  - 1 node at most is created per line OR
     *  - 1 relationship at most is created per line
     */
    static List<String> toStandaloneStatements(Iterable<String> rawLines) {
        Map<String, String> patterns = new HashMap<>();
        List<String> statements = new ArrayList<>();
        for (String line : rawLines) {
            Matcher createNodeStatement = NODE_CREATION.matcher(line);
            if (createNodeStatement.matches()) {
                patterns.put(createNodeStatement.group(1), createNodeStatement.group(2));
                continue;
            }
            Matcher createRelationStatement = RELATION_CREATION.matcher(line);
            if (createRelationStatement.matches()) {
                String statement = String.format(
                        "MERGE (start%s) MERGE (end%s) CREATE (start)-%s->(end)",
                        patterns.get(createRelationStatement.group(1)),
                        patterns.get(createRelationStatement.group(3)),
                        createRelationStatement.group(2)
                );
                statements.add(statement);
                continue;
            }
            statements.add(line);
        }
        return statements;
    }

    private static List<String> readLines(String resource) {
        try (InputStream file = DoctorWhoGraph.class.getResourceAsStream(resource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

            return parseStatements(reader).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Stream<String> parseStatements(BufferedReader reader) {
        return reader.lines()
                .filter(l -> !l.trim().isEmpty())
                .filter(l -> !l.equals("BEGIN"))
                .filter(l -> !l.equals("COMMIT"));
    }

    private static void executeBatch(GraphDatabaseService graphDb, Collection<String> batch) {
        try (Transaction transaction = graphDb.beginTx()) {
            for (String line : batch) {
                graphDb.execute(line);
            }

            transaction.success();
        }
    }
}
