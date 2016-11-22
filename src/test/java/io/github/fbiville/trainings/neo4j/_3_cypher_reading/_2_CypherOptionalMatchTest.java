package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.StreamOperations;
import io.github.fbiville.trainings.neo4j.internal.Tuple2;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.fbiville.trainings.neo4j.internal.Tuple2.tuple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class _2_CypherOptionalMatchTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_return_the_characters_and_the_things_they_may_own() throws Exception {
        // HINT: characters may not own anything
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should fetch characters and the things they own");

            var result = asTuples(graphDb.execute(cql), "character", "thing");

            assertThat(result)
                    .hasSize(169)
                    .contains(
                        tuple("Doctor", "Tardis"),
                        tuple("Doctor", "Sonic Screwdriver"),
                        tuple("Master", "Tardis"),
                        tuple("Rani", "Tardis"),
                        tuple("Meddling Monk", "Tardis"),
                        tuple("Ace", null),
                        tuple("Donna Noble", null));
        }

    }

    private Iterable<Tuple2<String,String>> asTuples(Result result, String characterColumn, String thingColumn) {
        return StreamOperations.create(result)
            .flatMap((row) -> extractTuple(row, characterColumn, thingColumn).stream().flatMap(Stream::of))
            .collect(Collectors.toList());
    }

    private Optional<Tuple2<String, String>> extractTuple(Map<String, Object> row, String characterKey, String thingKey) {
        assertThat(row.keySet())
            .withFailMessage("Query error: each row should have exactly 2 columns: '%s' and '%s'", characterKey, thingKey)
            .containsOnly("character", "thing");

        Object character = row.get(characterKey);
        assertThat(character)
            .withFailMessage("Query error: character must not be null.")
            .isNotNull();

        Object thing = row.get(thingKey);
        return Optional.of(tuple(
            String.valueOf(character),
            thing == null ? null : String.valueOf(thing)));
    }
}
