package io.github.fbiville.trainings.neo4j._1_core_api;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.fail;

/**
 * This class focuses on the core API for relationship creation.
 * NOTE: From now on, there will not be comments about removing `fail` calls but you still have to do it!
 */
public class _2_RelationshipWriteTest extends GraphTests {

    @Test
    public void should_create_relationship_between_existing_nodes() {
        long guybrushInternalId, elaineInternalId;
        try (Transaction transaction = graphDb.beginTx()) {
            Node guybrush = graphDb.createNode(Label.label("Character"));
            guybrush.setProperty("name", "Guybrush");
            guybrushInternalId = guybrush.getId();
            Node elaine = graphDb.createNode(Label.label("Character"));
            elaine.setProperty("name", "Elaine");
            elaineInternalId = elaine.getId();

            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Node elaine = graphDb.getNodeById(elaineInternalId); // DON'T DO THIS IN REAL CODE
            Node guybrush = graphDb.getNodeById(guybrushInternalId); // DON'T DO THIS IN REAL CODE
            guybrush.createRelationshipTo(elaine, RelationshipType.withName("LOVES"));
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Relationship relationship = graphOperations.getSingleRelationship();
            assertThat(relationship.getType().name()).isEqualTo("LOVES");
            assertThat(relationship.getAllProperties()).isEmpty();
            assertThat(relationship.getStartNode().getId()).isEqualTo(guybrushInternalId); // DON'T DO THIS IN REAL CODE
            assertThat(relationship.getEndNode().getId()).isEqualTo(elaineInternalId); // DON'T DO THIS IN REAL CODE
        }
    }

    @Test
    public void should_create_relationship_with_properties() {
        long guybrushInternalId, chuckInternalId;
        try (Transaction transaction = graphDb.beginTx()) {
            Node guybrush = graphDb.createNode(Label.label("Character"));
            guybrush.setProperty("name", "Guybrush");
            guybrushInternalId = guybrush.getId();
            Node chuck = graphDb.createNode(Label.label("Character"));
            chuck.setProperty("name", "LeChuck");
            chuckInternalId = chuck.getId();

            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Node guybrush = graphDb.getNodeById(guybrushInternalId);
            Node chuck = graphDb.getNodeById(chuckInternalId);

            Relationship relationship = guybrush
                .createRelationshipTo(chuck, RelationshipType.withName("HATES"));
            relationship.setProperty("reason", "Both Guybrush and LeChuck love Elaine");
            relationship.setProperty("level", "Extreme");
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Relationship relationship = graphOperations.getSingleRelationship();
            assertThat(relationship.getType().name()).isEqualTo("HATES");
            assertThat(relationship.getAllProperties()).containsOnly(
                entry("reason", "Both Guybrush and LeChuck love Elaine"),
                entry("level", "Extreme")
            );
            assertThat(relationship.getStartNode().getId()).isEqualTo(guybrushInternalId); // DON'T DO THIS IN REAL CODE
            assertThat(relationship.getEndNode().getId()).isEqualTo(chuckInternalId); // DON'T DO THIS IN REAL CODE
        }
    }

    @Test
    public void should_create_relationship_between_two_new_nodes() {
        try (Transaction transaction = graphDb.beginTx()) {
            Node character1 = graphDb.createNode(Label.label("Character"));
            Node character2 = graphDb.createNode(Label.label("Character"));
            Relationship relationship = character1.createRelationshipTo(character2, RelationshipType.withName("FIGHTS"));
            relationship.setProperty("fight_type", "Insult sword fighting");
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Relationship singleRelationship = graphOperations.getSingleRelationship();
            assertThat(singleRelationship.getType().name()).isEqualTo("FIGHTS");
            assertThat(singleRelationship.getAllProperties()).containsOnly(
                entry("fight_type", "Insult sword fighting")
            );
            Node startNode = singleRelationship.getStartNode();
            Node endNode = singleRelationship.getEndNode();
            assertThat(startNode)
                .isNotEqualTo(endNode)
                .extracting(graphOperations::getSingleLabelName)
                .containsOnly("Character");
            assertThat(endNode)
                .extracting(graphOperations::getSingleLabelName)
                .containsOnly("Character");
        }
    }

    @Test
    public void should_update_the_relationship_property() {
        try (Transaction transaction = graphDb.beginTx()) {
            Node node = graphDb.createNode();
            // Fun fact: a node can be linked to itself!
            Relationship relationship = node.createRelationshipTo(node, RelationshipType.withName("TALKS"));
            relationship.setProperty("message", "Hello wirld"); // same as Node

            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Relationship singleRelationship = graphOperations.getSingleRelationship();
            assertThat(singleRelationship.getType().name()).isEqualTo("TALKS");
            assertThat(singleRelationship.getAllProperties()).containsOnly(
                entry("message", "Hello wirld")
            );
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Relationship singleRelationship = graphOperations.getSingleRelationship();
            singleRelationship.setProperty("message", "Hello world");
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Relationship singleRelationship = graphOperations.getSingleRelationship();
            assertThat(singleRelationship.getType().name()).isEqualTo("TALKS");
            assertThat(singleRelationship.getAllProperties()).containsOnly(
                entry("message", "Hello world")
            );
        }
    }

    @Test
    public void should_remove_a_relationship() {
        try (Transaction transaction = graphDb.beginTx()) {
            graphDb.createNode().createRelationshipTo(graphDb.createNode(), RelationshipType.withName("TALKS"));
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Relationship singleRelationship = graphOperations.getSingleRelationship();
            singleRelationship.delete();
            transaction.success();
        }

        try (Transaction transaction = graphDb.beginTx()) {
            Iterable<Relationship> allRelationships = graphOperations.getAllRelationships();

            assertThat(allRelationships).isEmpty();
            transaction.success();
        }
    }
}
