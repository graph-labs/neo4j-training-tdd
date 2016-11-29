package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class _5_CypherCollectionTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_extract_the_first_five_episode_titles() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH p=(e:Episode {episode:'1'})-[:NEXT*4..4]->(:Episode)" +
                    "RETURN EXTRACT(e in nodes(p) | e.title) AS episodes";

            Result result = graphDb.execute(cql);

            ResourceIterator<Iterable<String>> episodes = result.columnAs("episodes");
            assertThat(episodes).containsOnly(asList(
                "An Unearthly Child",
                "The Daleks",
                "The Edge of Destruction",
                "Marco Polo",
                "The Keys of Marinus"));
        }
    }
}
