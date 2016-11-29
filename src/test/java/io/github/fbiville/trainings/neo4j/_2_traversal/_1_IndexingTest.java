package io.github.fbiville.trainings.neo4j._2_traversal;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.MonkeyIslandGraph;
import io.github.fbiville.trainings.neo4j.internal.StreamOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.graphdb.ConstraintViolationException;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.ConstraintType;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.kernel.api.exceptions.schema.UniquePropertyValueValidationException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.isA;

public class _1_IndexingTest extends GraphTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void prepare() {
        MonkeyIslandGraph.create(graphDb);
    }

    @Test
    public void should_index_existing_characters_by_name() {
        try (Transaction transaction = graphDb.beginTx()) {
            Index<Node> index = graphDb.index().forNodes("characters");
            Iterator<Node> characters = graphDb.findNodes(Label.label("Character"));
            while (characters.hasNext()) {
                Node character = characters.next();
                index.add(character, "indexed_name", character.getProperty("name"));
            }

            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            IndexManager indexManager = graphDb.index();
            assertThat(filter(indexManager.nodeIndexNames())).containsOnly("characters");
            Index<Node> index = indexManager.forNodes("characters");
            /*
             * Lucene syntax: find any two words separated by space on field indexed_name
             * That excludes "LeChuck" because his name does not contain any space
             */
            Iterator<Node> results = index.query("indexed_name:*\\ *");
            assertThat(results)
                .hasSize(3)
                .extracting(node -> node.getProperty("name"))
                .containsOnly("Guybrush Threepwood", "Elaine Marley", "Largo LaGrande");
            transaction.success();
        }
    }

    @Test
    public void should_automatically_index_names_in_upper_case() {
        try (Transaction transaction = graphDb.beginTx()) {
            AutoIndexer<Node> nodeAutoIndexer = graphDb.index().getNodeAutoIndexer();
            nodeAutoIndexer.setEnabled(true);
            nodeAutoIndexer.startAutoIndexingProperty("name");
            graphDb.findNodes(Label.label("Character"))
                .forEachRemaining(node -> {
                    node.setProperty("name", node.getProperty("name").toString().toUpperCase(Locale.ENGLISH));
                });
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            AutoIndexer<Node> nodeAutoIndexer = graphDb.index().getNodeAutoIndexer();
            assertThat(nodeAutoIndexer.isEnabled()).isTrue();
            assertThat(nodeAutoIndexer.getAutoIndexedProperties()).containsOnly("name");
            ReadableIndex<Node> index = nodeAutoIndexer.getAutoIndex();
            Iterator<Node> results = index.query("name:*\\ *");
            assertThat(results)
                .hasSize(3)
                .extracting(node -> node.getProperty("name"))
                .containsOnly("GUYBRUSH THREEPWOOD", "ELAINE MARLEY", "LARGO LAGRANDE");
            transaction.success();
        }
    }

    @Test
    public void should_create_index_for_character_names() {
        try (Transaction transaction = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            schema.indexFor(Label.label("Character")).on("name").create();
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            Iterable<IndexDefinition> indices = schema.getIndexes();
            assertThat(indices).hasSize(1);
            IndexDefinition index = indices.iterator().next();
            assertThat(index.isConstraintIndex()).isFalse();
            assertThat(index.getLabel().name()).isEqualTo("Character");
            assertThat(index.getPropertyKeys()).containsOnly("name");
            transaction.success();
        }
    }

    @Test
    public void should_create_character_name_unicity_constraint() {
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.schema().constraintFor(Label.label("Character"))
                .assertPropertyIsUnique("name")
                .create();
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Schema schema = graphDb.schema();
            Iterable<ConstraintDefinition> constraints = schema.getConstraints();
            assertThat(constraints).hasSize(1);
            ConstraintDefinition constraint = constraints.iterator().next();
            assertThat(constraint.getLabel().name()).isEqualTo("Character");
            assertThat(constraint.getPropertyKeys()).containsOnly("name");
            assertThat(constraint.getConstraintType()).isEqualTo(ConstraintType.UNIQUENESS);
            transaction.success();
        }

        // double-check the unicity constraint is active:
        // a character with the same name should not be inserted
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("already exists with label `Character` and property `name` = 'LeChuck'");
        thrown.expectCause(isA(UniquePropertyValueValidationException.class));
        try (Transaction transaction = graphDb.beginTx()) {
            Node character = graphDb.createNode(Label.label("Character"));
            character.setProperty("name", "LeChuck");
            transaction.success();
        }
    }

    private Collection<String> filter(String[] indices) {
        return StreamOperations.create(indices)
            .filter(Predicate.isEqual("node_auto_index").negate())
            .collect(Collectors.toList());
    }

}
