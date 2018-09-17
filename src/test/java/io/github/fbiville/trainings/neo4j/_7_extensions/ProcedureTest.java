package io.github.fbiville.trainings.neo4j._7_extensions;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.harness.junit.Neo4jRule;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcedureTest extends GraphTests {

    @Rule
    public Neo4jRule rule = new Neo4jRule().withProcedure(CountEpisodes.class);
    private GraphDatabaseService graphDb;

    @Before
    public void prepare() {
        graphDb = rule.getGraphDatabaseService();
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_not_find_episodes_for_unrelated_characters() {
        try (Transaction ignored = graphDb.beginTx();
             Result result = graphDb.execute("CALL io.github.fbiville.trainings.neo4j._7_extensions.count_episodes('Luke Skywalker')");) {

            assertThat(result.columnAs("value")).containsOnly(0L);
        }
    }

    @Test
    public void should_find_episodes_for_Tom_Baker() {
        try (Transaction ignored = graphDb.beginTx();
             Result result = graphDb.execute("CALL io.github.fbiville.trainings.neo4j._7_extensions.count_episodes('Tom Baker')");) {

            assertThat(result.columnAs("value")).containsOnly(43L);
        }
    }
}
