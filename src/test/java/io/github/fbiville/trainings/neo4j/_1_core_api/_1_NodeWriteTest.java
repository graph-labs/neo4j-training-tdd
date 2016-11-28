package io.github.fbiville.trainings.neo4j._1_core_api;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.fail;

/**
 * This class focuses on the core API for node creation.
 */
public class _1_NodeWriteTest extends GraphTests {

    @Test
    public void writes_simple_node() {
        // every operation must be surrounded by a transaction
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.createNode();
            transaction.success(); // commit transaction so that data is created on disk
        }

        /*
         * every operation must be surrounded by a transaction
         * here the transaction could be committed, but nothing changed, so no need
         * in that case: the transaction will roll back
         */
        try (Transaction ignored = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            assertThat(node.getLabels()).isEmpty();
            assertThat(node.getAllProperties()).isEmpty();
            assertThat(node.getRelationships()).isEmpty();
        }
    }

    @Test
    public void writes_node_with_a_single_label() {
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.createNode(Label.label("Video game"));
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            assertThat(node.getLabels()).extracting(Label::name).containsExactly("Video game");
            assertThat(node.getAllProperties()).isEmpty();
            assertThat(node.getRelationships()).isEmpty();
        }
    }

    @Test
    public void writes_node_with_several_labels() {
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.createNode(Label.label("Video game"), Label.label("Adventure"));
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            assertThat(node.getLabels())
                .extracting(Label::name)
                .containsOnly("Video game", "Adventure");
            assertThat(node.getAllProperties()).isEmpty();
            assertThat(node.getRelationships()).isEmpty();
        }
    }

    @Test
    public void writes_node_with_properties() {
        try (Transaction transaction = graphDb.beginTx()) {
            Node node = graphDb.createNode();
            node.setProperty("title", "Monkey Island");
            node.setProperty("price_in_EUR", 50);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            assertThat(node.getLabels()).isEmpty();
            assertThat(node.getAllProperties()).containsOnly(
                entry("title", "Monkey Island"),
                entry("price_in_EUR", 50)
            );
            assertThat(node.getRelationships()).isEmpty();
        }
    }

    @Test
    public void writes_node_with_several_labels_and_properties() {
        try (Transaction transaction = graphDb.beginTx()) {
            Node node = graphDb.createNode(Label.label("Character"), Label.label("Pirate"));
            node.setProperty("name", "Guybrush Threepwood");
            node.setProperty("age", 42);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            assertThat(node.getLabels())
                .extracting(Label::name)
                .containsOnly("Character", "Pirate");
            assertThat(node.getAllProperties()).containsOnly(
                entry("name", "Guybrush Threepwood"),
                entry("age", 42)
            );
            assertThat(node.getRelationships()).isEmpty();
        }
    }
    
    @Test
    public void deletes_a_node() {
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.createNode();
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Node node = graphOperations.getSingleNode();
            node.delete();

            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            assertThat(graphOperations.getAllNodes()).isEmpty();
        }
    }

    @Test
    public void removes_a_property() {
        try (Transaction transaction = graphDb.beginTx()) {
            Node largo = graphDb.createNode(Label.label("Character"));
            largo.setProperty("name", "Largo");
            largo.setProperty("height", "Small");

            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node largo = graphOperations.getSingleNode();
            assertThat(largo.getLabels()).extracting(Label::name).containsOnly("Character");
            assertThat(largo.getAllProperties()).containsOnly(
                entry("name", "Largo"),
                entry("height", "Small")
            );
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Node largo = graphOperations.getSingleNode();
            largo.removeProperty("height");

            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Node largo = graphOperations.getSingleNode();
            assertThat(largo.getLabels()).extracting(Label::name).containsOnly("Character");
            assertThat(largo.getAllProperties()).containsOnly(
                entry("name", "Largo")
            );
        }
    }
}
