package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Maps.newHashMap;

public class _4_CypherPathTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_find_how_many_regenerations_between_tom_baker_and_christopher_eccleston() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH path=(:Actor {actor:'Tom Baker'})-[:REGENERATED_TO*]->(:Actor {actor:'Christopher Eccleston'})" +
                    "RETURN LENGTH(path) AS regenerations";

            Result result = graphDb.execute(cql);

            assertThat(result).containsOnly(newHashMap("regenerations", 5L));
        }
    }

    @Test
    public void should_find_the_longest_continuous_story_arc_with_the_master() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH (master:Character {character:'Master'})-[:APPEARED_IN]->(first:Episode), arcs = (first)-[:NEXT*]->() " +
                    "WHERE ALL(ep IN nodes(arcs) WHERE (master)-[:APPEARED_IN]->(ep)) " +
                    "RETURN LENGTH(arcs) AS noOfPathHops " +
                    "ORDER BY noOfPathHops DESC " +
                    "LIMIT 1";

            Result result = graphDb.execute(cql);

            // noOfPathHops is one less than the number of episodes in a story arc
            assertThat(result).containsOnly(newHashMap("noOfPathHops", 4L));
        }
    }

    @Test
    public void should_find_length_of_the_shortest_path_between_sarah_jane_smith_and_skaro() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH path=shortestPath((sarah:Character {character:'Sarah Jane Smith'})-[*]-(skaro:Planet {planet: 'Skaro'})) " +
                    "RETURN LENGTH(path) AS length";

            Result result = graphDb.execute(cql);

            assertThat(result).containsOnly(newHashMap("length", 3L));
        }
    }
}
