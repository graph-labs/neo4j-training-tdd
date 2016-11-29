package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

public class _6_CypherSubQueryTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_aggregate_and_sort_companions_alphabetically() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (c:Character)-[:COMPANION_OF]->(:Character {character:'Doctor'}) " +
                    "WITH c " +
                    "ORDER BY c.character " +
                    "RETURN COLLECT(c.character) AS characters";

            Result result = graphDb.execute(cql);

            ResourceIterator<Iterable<String>> characters = result.columnAs("characters");
            assertThat(characters.hasNext()).isTrue();
            assertThat(characters.next()).containsExactly(
                "Ace",
                "Adam Mitchell",
                "Adelaide Brooke",
                "Adric",
                "Amy Pond",
                "Astrid Peth",
                "Barbara Wright",
                "Ben Jackson",
                "Craig Owens",
                "Dodo Chaplet",
                "Donna Noble",
                "Grace Holloway",
                "Hamish Wilson",
                "Harry Sullivan",
                "Ian Chesterton",
                "Jack Harkness",
                "Jackson Lake",
                "Jamie McCrimmon",
                "Jo Grant",
                "K9",
                "Kamelion",
                "Katarina",
                "Lady Christina de Souza",
                "Leela",
                "Liz Shaw",
                "Martha Jones",
                "Melanie Bush",
                "Mickey Smith",
                "Nyssa",
                "Peri Brown",
                "Polly",
                "River Song",
                "Romana",
                "Rory Williams",
                "Rose Tyler",
                "Rosita Farisi",
                "Sara Kingdom",
                "Sarah Jane Smith",
                "Steven Taylor",
                "Susan Foreman",
                "Tegan Jovanka",
                "Vicki",
                "Victoria Waterfield",
                "Vislor Turlough",
                "Wilfred Mott",
                "Zoe Heriot");
            assertThat(characters.hasNext()).isFalse();
        }

    }

    @Test
    public void should_find_the_popular_companions_who_appeared_more_than_twenty_times() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH (:Episode)<-[a:APPEARED_IN]-(c:Character)-[:COMPANION_OF]->(:Character {character:'Doctor'}) " +
                    "WITH COLLECT(a) AS episodes, c.character AS companions " +
                    "WHERE LENGTH(episodes) > 20 " +
                    "RETURN companions";

            Result result = graphDb.execute(cql);

            assertThat(result.<String>columnAs("companions")).containsOnly(
                "Amy Pond",
                "Sarah Jane Smith",
                "Rose Tyler",
                "Jamie McCrimmon");
        }
    }
}
