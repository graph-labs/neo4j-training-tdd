package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.IteratorOperations;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class _1_CypherSimpleMatchTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_find_all_the_episodes_using_labels() {
        /*
         * HINT: The number of episodes is not the same as the highest episode number
         * Some episodes are two-parters with the same episode number, others use schemes like
         * 218a and 218b as their episode numbers seemingly just to be difficult!
         */
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (e:Episode) RETURN e";

            try (Result result = graphDb.execute(cql)) {
                assertThat(IteratorOperations.size(result)).isEqualTo(246);
            }
        }
    }

    @Test
    public void should_find_all_the_episodes_in_which_the_cybermen_appeared() {
        //HINT: there is a direct relation between Species and Episode ;)
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (:Species {species:'Cyberman'})-[:APPEARED_IN]->(episode:Episode) RETURN episode";

            ResourceIterator<Node> result = graphDb.execute(cql).columnAs("episode");

            List<String> expectedTitles = asList("The Next Doctor",
                                                 "The Pandorica Opens",
                                                 "Army of Ghosts",
                                                 "Doomsday",
                                                 "Rise of the Cybermen",
                                                 "The Age of Steel",
                                                 "Earthshock",
                                                 "Silver Nemesis",
                                                 "The Wheel in Space",
                                                 "Revenge of the Cybermen",
                                                 "The Moonbase",
                                                 "The Tomb of the Cybermen",
                                                 "Closing Time",
                                                 "A Good Man Goes to War");

            assertThat(result)
                    .extracting(n -> (String) n.getProperty("title"))
                    .hasSameSizeAs(expectedTitles)
                    .hasSameElementsAs(expectedTitles);
        }
    }

    @Test
    public void should_find_episodes_where_tennant_and_rose_battle_the_daleks() throws Exception {
        // HINT: David Tennant is an actor
        // HINT: Rose Tyler is a character
        // HINT: Dalek is a species

        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (:Character {character:'Rose Tyler'})-[:APPEARED_IN]->(e:Episode)<-[:APPEARED_IN]-(:Actor {actor:'David Tennant'})," +
                    "   (:Species {species:'Dalek'})-[:APPEARED_IN]->(e) RETURN e AS episode";

            Iterator<Node> result = graphDb.execute(cql).columnAs("episode");

            List<String> expectedTitles = asList("Journey's End",
                    "The Stolen Earth",
                    "Doomsday",
                    "Army of Ghosts",
                    "The Parting of the Ways");

            assertThat(result)
                    .extracting(n -> (String) n.getProperty("title"))
                    .hasSameSizeAs(expectedTitles)
                    .hasSameElementsAs(expectedTitles);
        }
    }

    @Test
    public void should_return_any_wikipedia_entries_for_companions() {
        // HINT: COMPANION_OF whom? of Character "Doctor"!
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (companion:Character)-[:COMPANION_OF]->(:Character {character:'Doctor'}) " +
                    "WHERE EXISTS(companion.wikipedia) " +
                    "RETURN companion.wikipedia";

            Iterator<String> result = graphDb.execute(cql).columnAs("companion.wikipedia");

            List<String> expectedEntries = asList("http://en.wikipedia.org/wiki/Rory_Williams",
                                                  "http://en.wikipedia.org/wiki/Amy_Pond",
                                                  "http://en.wikipedia.org/wiki/River_Song_(Doctor_Who)");

            assertThat(result)
                    .hasSameSizeAs(expectedEntries)
                    .hasSameElementsAs(expectedEntries);
        }
    }

    @Test
    public void should_find_individual_companions_and_enemies_of_the_doctor() {
        // hint: *individual*
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (c:Character)-[:ENEMY_OF|:COMPANION_OF]->(:Character {character:'Doctor'}) RETURN COUNT(c) AS count";

            Iterator<Long> result = graphDb.execute(cql).columnAs("count");

            assertThat(result)
                    .hasSize(1)
                    .containsOnly(149L);
        }
    }

}
