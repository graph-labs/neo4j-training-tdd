package io.github.fbiville.trainings.neo4j._4_cypher_writing;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Maps.newHashMap;

/*
 * Please note that each test in this class starts with an
 * empty graph database: the DoctorWho graph is not imported.
 */
public class _2_CypherCreateDeleteTest extends GraphTests {

    @Test
    public void should_create_a_single_node() {
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = "CREATE (n)";
            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            ResourceIterable<Node> allNodes = graphDb.getAllNodes();
            assertThat(allNodes).hasSize(1);
            Node node = allNodes.iterator().next();
            assertThat(node.getLabels()).isEmpty();
            assertThat(node.getRelationships()).isEmpty();
            assertThat(node.getAllProperties()).isEmpty();
        }
    }

    @Test
    public void should_create_a_single_node_with_some_properties() {
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = "CREATE (:Actor {firstname:'Tom', lastname:'Baker'})";

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            ResourceIterable<Node> allNodes = graphDb.getAllNodes();
            assertThat(allNodes).hasSize(1);
            Node node = allNodes.iterator().next();
            assertThat(node.getLabels()).containsOnly(Label.label("Actor"));
            assertThat(node.getRelationships()).isEmpty();
            Map<String,Object> expectedProperties = new HashMap<>();
            expectedProperties.put("firstname", "Tom");
            expectedProperties.put("lastname", "Baker");
            assertThat(node.getAllProperties()).isEqualTo(expectedProperties);
        }
    }

    @Test
    public void should_create_a_simple_connected_graph() {
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = "CREATE (:Character {character:'Doctor'})<-[:ENEMY_OF]-(:Character {character:'Master'})";

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            ResourceIterator<Node> characters = graphDb.findNodes(Label.label("Character"));
            assertThat(characters).hasSize(2)
                .extracting(Node::getAllProperties)
                .containsOnly(newHashMap("character", "Doctor"), newHashMap("character", "Master"));
            ResourceIterable<RelationshipType> allRelationships = graphDb.getAllRelationshipTypesInUse();
            assertThat(allRelationships).hasSize(1)
                .extracting(RelationshipType::name)
                .containsOnly("ENEMY_OF");
        }
    }
}
