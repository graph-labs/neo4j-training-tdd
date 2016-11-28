package io.github.fbiville.trainings.neo4j._7_extensions;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CountEpisodes {

    @Context
    public GraphDatabaseService graphDb;

    @Procedure
    public Stream<Count> count_episodes(@Name("name") String actor) {
        try (Transaction ignored = graphDb.beginTx()) {
            /* TODO: write and execute query */
            return Stream.of(new Count(-1L));
        }
    }

    private Map<String, Object> parameters(String actor) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("actor", actor);
        return parameters;
    }
}

