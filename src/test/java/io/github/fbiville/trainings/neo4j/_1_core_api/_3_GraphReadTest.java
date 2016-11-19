package io.github.fbiville.trainings.neo4j._1_core_api;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.MonkeyIslandGraph;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.fail;

/**
 * This class focuses on the basic read operations.
 */
public class _3_GraphReadTest extends GraphTests {

    /*
     * This method is executed before each test, on an empty graph database
     * This is going to be the same graph for every following test.
     *
     * Take some time to understand what is being inserted!
     */
    @Before
    public void prepare() {
        MonkeyIslandGraph.create(graphDb);
    }

    @Test
    public void should_find_all_character_nodes() {
        try (Transaction ignored = graphDb.beginTx()) {
            Iterator<Node> allNodes = null; /*TODO: find all nodes*/
            fail("You should find all nodes");

            assertThat(allNodes)
                .hasSize(4)
                .extracting(node -> (String) node.getProperty("name"))
                .containsOnly("Guybrush Threepwood", "Elaine Marley", "LeChuck", "Largo LaGrande");
        }
    }

    @Test
    public void should_find_all_heroes() {
        try (Transaction ignored = graphDb.beginTx()) {
            Iterator<Node> allNodes = null/*TODO: find all heroes*/;
            fail("You should find all heroes");

            assertThat(allNodes)
                .hasSize(2)
                .extracting(node -> (String) node.getProperty("name"))
                .containsOnly("Guybrush Threepwood", "Elaine Marley");
        }
    }

    @Test
    public void should_find_all_villains() {
        try (Transaction ignored = graphDb.beginTx()) {
            Iterator<Node> allNodes = null/*TODO: find all villains*/;
            fail("You should find all villains");

            assertThat(allNodes)
                .hasSize(2)
                .extracting(node -> (String) node.getProperty("name"))
                .containsOnly("LeChuck", "Largo LaGrande");
        }
    }

    @Test
    public void should_find_all_relationship_types() {
        try (Transaction ignored = graphDb.beginTx()) {
            Iterable<RelationshipType> allRelationships = null/*TODO: find all relationships*/;
            fail("You should find all relationships");

            assertThat(allRelationships)
                .hasSize(4)
                .extracting(RelationshipType::name)
                .containsOnly("LOVES", "FIGHTS", "HATES", "APPEARS_IN");
        }
    }

    @Test
    public void should_find_largo_by_label_and_property() {
        try (Transaction ignored = graphDb.beginTx()) {
            Node largo = null/*TODO: find Largo LaGrande node*/;
            fail("You should find a Largo LaGrande node");

            assertThat(largo.getLabels())
                .extracting(Label::name)
                .containsOnly("Character", "Villain");
            assertThat(largo.getAllProperties()).containsOnly(
                entry("name", "Largo LaGrande"),
                entry("in_first_opus", false)
            );
            assertThat(largo.getRelationships())
                .extracting(Relationship::getType)
                .extracting(RelationshipType::name)
                .containsOnly("FIGHTS", "HATES", "APPEARS_IN");
        }
    }

    @Test
    public void should_find_characters_appearing_in_first_opus() {
        try (Transaction ignored = graphDb.beginTx()) {
            Iterator<Node> firstOpusCharacters = null/*TODO: find all first opus character nodes*/;
            fail("You should find all character nodes appearing in the first opus");

            assertThat(firstOpusCharacters)
                .extracting(node -> (String) node.getProperty("name"))
                .containsOnly("Guybrush Threepwood", "Elaine Marley", "LeChuck");
        }
    }
}
