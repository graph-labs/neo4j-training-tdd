package io.github.fbiville.trainings.neo4j._4_cypher_writing;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Maps.newHashMap;

public class _3_CypherMergeTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_bring_actors_Karen_Gillan_and_Caitlin_Blackwood_into_the_Amy_Pond_subgraph() {
        // Hint: Amy Pond is definitely in the graph, and the actors may be too. How can MERGE help?
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should add Karen Gillan and Caitlin Blackwood to Amy Pond character graph");

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            ResourceIterator<String> actors = graphDb.execute("MATCH (:Character {character: 'Amy Pond'})<-[:PLAYED]-(a:Actor) " +
                "RETURN a.actor AS actor").columnAs("actor");
            assertThat(actors).containsOnly("Karen Gillan", "Caitlin Blackwood");
            assertThat(count("Amy Pond", "character")).isEqualTo(1L);
            assertThat(count("Karen Gillan", "actor")).isEqualTo(1L);
            assertThat(count("Caitlin Blackwood", "actor")).isEqualTo(1L);
        }
    }

    @Test
    public void should_make_sure_Amy_Pond_and_Rory_Williams_are_in_love() {
        // Hint: love must go both ways ;-D
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should make Amy Pond and Rory Williams are in love");

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Result result = graphDb.execute("MATCH (:Character {character: 'Amy Pond'})-[loves:LOVES]->(:Character {character: 'Rory Williams'}) RETURN loves");
            assertThat(result).hasSize(1);
            result = graphDb.execute("MATCH (:Character {character: 'Amy Pond'})<-[loves:LOVES]-(:Character {character: 'Rory Williams'}) RETURN loves");
            assertThat(result).hasSize(1);
        }
    }

    @Test
    public void should_demarcate_years_when_Amy_Pond_was_a_companion_of_the_doctor() {
        // Hint: you should set the dates only if it matches
        try (Transaction transaction = graphDb.beginTx()) {
            String cql = null /*TODO: write Cypher query*/;
            fail("You should set the years of companionship of Amy Pond with Doctor Who");

            graphDb.execute(cql);
            transaction.success();
        }

        try (Transaction ignored = graphDb.beginTx()) {
            Result result = graphDb.execute("MATCH (:Character {character: 'Amy Pond'})-[c:COMPANION_OF {`start`:2010, `end`:2013}]->(:Character {character: 'Doctor'}) RETURN c");
            assertThat(result).hasSize(1);
        }
    }

    private Long count(String character, String type) {
        Map<String, Object> parameters = newHashMap("name", character);
        String label = label(type);
        String variable = variable(type);
        String property = property(type);
        String query = String.format("MATCH (%1$s:%2$s {%3$s: {name}}) RETURN count(%1$s) AS count", variable, label, property);
        return (Long) graphDb.execute(query, parameters).columnAs("count").next();
    }

    private String label(String type) {
        return Character.toUpperCase(type.charAt(0)) + type.substring(1).toLowerCase(Locale.ENGLISH);
    }

    private String variable(String type) {
        return type.toLowerCase(Locale.ENGLISH).charAt(0) + "";
    }

    private String property(String type) {
        return type.toLowerCase(Locale.ENGLISH);
    }
}
