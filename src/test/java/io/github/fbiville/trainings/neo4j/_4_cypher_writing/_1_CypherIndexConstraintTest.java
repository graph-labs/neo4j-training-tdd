package io.github.fbiville.trainings.neo4j._4_cypher_writing;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.core.StringContains.containsString;

public class _1_CypherIndexConstraintTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_create_an_index_of_species_with_Cypher() {
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should write an index for species");

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Label expectedLabel = Label.label("Species");
            Iterable<IndexDefinition> speciesIndices = graphDb.schema().getIndexes(expectedLabel);
            assertThat(speciesIndices).hasSize(1);
            IndexDefinition index = speciesIndices.iterator().next();
            assertThat(index.getLabel()).isEqualTo(expectedLabel);
            assertThat(index.getPropertyKeys()).containsOnly("species");
            assertThat(index.isConstraintIndex()).isFalse();
        }
    }

    @Test
    public void should_drop_species_index_with_Cypher() {
        should_create_an_index_of_species_with_Cypher();

        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should delete the index for species");

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Label expectedLabel = Label.label("Species");
            assertThat(graphDb.schema().getIndexes(expectedLabel)).isEmpty();
        }
    }

    @Test/* Acting trivia: American and British actors' unions demand that no two actors have the same name! */
    public void should_disallow_duplicate_actor_creation() {
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should disallow duplicate actor creation");

            graphDb.execute(cql);
            transaction.success();
        }

        thrown.expect(QueryExecutionException.class);
        thrown.expectMessage(containsString("already exists with label `Actor` and property `actor` = 'David Tennant'"));
        try (Transaction ignored = graphDb.beginTx()) {
            graphDb.execute("CREATE (:Actor {actor: 'David Tennant'})");
        }

    }

}
