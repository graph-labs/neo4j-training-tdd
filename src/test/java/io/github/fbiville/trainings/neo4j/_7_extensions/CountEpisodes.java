package io.github.fbiville.trainings.neo4j._7_extensions;

import org.assertj.core.util.Maps;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
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

    @Procedure(name = "io.github.fbiville.trainings.neo4j._7_extensions.count_episodes")
    public Stream<Count> count_episodes(@Name("name") String actor) {
        try (Transaction ignored = graphDb.beginTx()) {
            String query = "MATCH (a:Actor {actor: {c}}) " +
                    "OPTIONAL MATCH (a)-[ai:APPEARED_IN]->(:Episode) " +
                    "RETURN COUNT(ai) AS count";
            try (Result result = graphDb.execute(query, Maps.newHashMap("c", actor));
                 ResourceIterator<Long> iterator = result.columnAs("count")) {

                return Stream.of(new Count(iterator.next()));
            }
        }
    }

    private Map<String, Object> parameters(String actor) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("actor", actor);
        return parameters;
    }
}

