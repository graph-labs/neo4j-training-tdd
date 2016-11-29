package io.github.fbiville.trainings.neo4j._3_cypher_reading;

import io.github.fbiville.trainings.neo4j.internal.DoctorWhoGraph;
import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.StreamOperations;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static io.github.fbiville.trainings.neo4j._3_cypher_reading._3_CypherAggregationTest.DoctorWhoCharacter.character;
import static io.github.fbiville.trainings.neo4j._3_cypher_reading._3_CypherAggregationTest.PeterDavisonEpisode.episode;
import static io.github.fbiville.trainings.neo4j._3_cypher_reading._3_CypherAggregationTest.Species.species;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Maps.newHashMap;

public class _3_CypherAggregationTest extends GraphTests {

    @Before
    public void prepare() {
        DoctorWhoGraph.create(graphDb);
    }

    @Test
    public void should_count_the_number_of_actors_known_to_have_played_the_doctor() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (a:Actor)-[:PLAYED]->(:Character {character:'Doctor'}) RETURN COUNT(a) AS numberOfActorsWhoPlayedTheDoctor";

            Result result = graphDb.execute(cql);

            assertThat(result).containsOnly(newHashMap("numberOfActorsWhoPlayedTheDoctor", 12L));
        }
    }

    @Test
    public void should_find_earliest_and_latest_regeneration_years() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH ()-[r:REGENERATED_TO]->() " +
                    "RETURN MIN(r.year) AS earliest, MAX(r.year) AS latest";

            Result result = graphDb.execute(cql);

            Map<String, Object> expectedRow = new HashMap<>();
            expectedRow.put("latest", 2010L);
            expectedRow.put("earliest", 1966L);
            assertThat(result).containsOnly(expectedRow);
        }

    }

    @Test
    public void should_find_the_earliest_episode_where_freema_agyeman_and_david_tennant_worked_together() {
        // HINT: you will need to convert the result type in Cypher
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (:Actor {actor:'David Tennant'})-[:APPEARED_IN]->(e:Episode)<-[:APPEARED_IN]-()<-[:PLAYED]-(:Actor {actor:'Freema Agyeman'}) " +
                    "RETURN MIN(TOINT(e.episode)) AS earliest";

            Result result = graphDb.execute(cql);

            assertThat(result).containsOnly(newHashMap("earliest", 179L));
        }
    }

    @Test
    public void should_find_average_salary_of_actors_who_played_the_doctor() {
        // HINT: ignore the actors without salaries
        try (Transaction ignored = graphDb.beginTx()) {
            String cql = "MATCH (a:Actor)-[:PLAYED]->(:Character {character:'Doctor'}) " +
                    "RETURN AVG(a.salary) AS cash";
            Result result = graphDb.execute(cql);

            assertThat(result).containsOnly(newHashMap("cash", 600000.0));
        }
    }

    @Test
    public void should_list_the_enemy_species_and_enemy_characters_for_each_episode_featuring_peter_davison_ordered_in_chronological_order() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH (:Actor {actor:'Peter Davison'})-[:APPEARED_IN]->(episode:Episode), " +
                    "(episode)<-[:APPEARED_IN]-(e)-[:ENEMY_OF]->(:Character {character:'Doctor'}) " +
                    "RETURN episode.episode, episode.title, COLLECT(e.species) AS species, COLLECT(e.character) AS characters " +
                    "ORDER BY episode.episode";

            Result result = graphDb.execute(cql);

            assertThat(result.columns()).containsExactly("episode.episode", "episode.title", "species", "characters");
            assertThat(transformToEpisodes(result))
                .containsExactly(
                    episode("116", "Castrovalva",                   character("Master")),
                    episode("118", "Kinda",                         character("Mara")),
                    episode("119", "The Visitation",                character("Terileptils")),
                    episode("121", "Earthshock",                    species("Cyberman")),
                    episode("122", "Time-Flight",                   character("Master")),
                    episode("123", "Arc of Infinity",               character("Omega")),
                    episode("124", "Snakedance",                    character("Mara")),
                    episode("125", "Mawdryn Undead",                character("Mawdryn"), character("Black Guardian")),
                    episode("126", "Terminus",                      character("Vanir")),
                    episode("127", "Enlightenment",                 character("Black Guardian")),
                    episode("128", "The King's Demons",             character("Master")),
                    episode("129", "The Five Doctors",              singletonList(species("Dalek")), singletonList(character("Master"))),
                    episode("130", "Warriors of the Deep",          species("Sea Devil"), species("Silurian")),
                    episode("131", "The Awakening",                 character("Malus")),
                    episode("132", "Frontios",                      species("Tractator")),
                    episode("133", "Resurrection of the Daleks",    species("Dalek")),
                    episode("134", "Planet of Fire",                character("Master")),
                    episode("135", "The Caves of Androzani",        character("Master"))
                );
        }
    }

    @Test
    public void should_find_the_enemy_species_that_rose_tyler_fought() {
        try (Transaction ignored = graphDb.beginTx()) {
            String cql =
                    "MATCH (rose:Character {character:'Rose Tyler'})-[:APPEARED_IN]->(ep:Episode)," +
                    "(:Character {character:'Doctor'})-[:ENEMY_OF]->(enemy:Species)-[:APPEARED_IN]->(ep) " +
                    "RETURN DISTINCT enemy.species AS enemySpecies";

            ResourceIterator<String> result = graphDb.execute(cql).columnAs("enemySpecies");

            assertThat(result).containsOnly("Krillitane",
                "Sycorax",
                "Cyberman",
                "Dalek",
                "Auton",
                "Slitheen",
                "Clockwork Android");
        }
    }

    @Test
    public void should_find_the_hardest_working_prop_part_in_showbiz() {
        try (Transaction ignored = graphDb.beginTx()) {
            /*
             * HINT: you must find the original prop and its related part
             * that appear in the most episodes involving Dalek
             */
            String cql =
                    "MATCH (daleks:Species {species: \"Dalek\"})-[:APPEARED_IN]->(episode:Episode)<-[:USED_IN]-(:Props)" +
                    "<-[:MEMBER_OF]-(:Prop)-[:COMPOSED_OF]->(part:Part)-[:ORIGINAL_PROP]->(originalprop:Prop) " +
                    "RETURN originalprop.prop, part.part, count(DISTINCT episode) AS episode_count " +
                    "ORDER BY episode_count " +
                    "DESC LIMIT 1";

            Result result = graphDb.execute(cql);

            Map<String, Object> expectedRow = new HashMap<>();
            expectedRow.put("originalprop.prop", "Goon II");
            expectedRow.put("part.part", "skirt");
            expectedRow.put("episode_count", 12L);
            assertThat(result).containsOnly(expectedRow);
        }
    }

    private List<PeterDavisonEpisode> transformToEpisodes(Result result) {
        return StreamOperations.create(result)
            .map(PeterDavisonEpisode::fromRow)
            .collect(toList());
    }

    static class PeterDavisonEpisode {

        private final String episodeId;
        private final String episodeTitle;
        private final Iterable<Species> species;
        private final Iterable<DoctorWhoCharacter> characters;

        @SuppressWarnings("unchecked")
        public static PeterDavisonEpisode fromRow(Map<String, Object> row) {
            return new PeterDavisonEpisode(
                String.valueOf(row.get("episode.episode")),
                String.valueOf(row.get("episode.title")),
                mapIterableColumn((Iterable<String>)row.get("species"), Species::new),
                mapIterableColumn((Iterable<String>)row.get("characters"), DoctorWhoCharacter::new));
        }

        public static PeterDavisonEpisode episode(String id, String title, DoctorWhoCharacter... characters) {
            return new PeterDavisonEpisode(id, title, emptyList(), asList(characters));
        }

        public static PeterDavisonEpisode episode(String id, String title, Species... species) {
            return new PeterDavisonEpisode(id, title, asList(species), emptyList());
        }

        public static PeterDavisonEpisode episode(String id,
                                                  String title,
                                                  List<Species> species,
                                                  List<DoctorWhoCharacter> characters) {

            return new PeterDavisonEpisode(id, title, species, characters);
        }

        private PeterDavisonEpisode(String episodeId,
                                    String episodeTitle,
                                    List<Species> species,
                                    List<DoctorWhoCharacter> characters) {

            Collections.sort(species);
            Collections.sort(characters);

            this.episodeId = episodeId;
            this.episodeTitle = episodeTitle;
            this.species = species;
            this.characters = characters;
        }

        @Override
        public int hashCode() {
            return Objects.hash(episodeId, episodeTitle, species, characters);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final PeterDavisonEpisode other = (PeterDavisonEpisode) obj;
            return Objects.equals(this.episodeId, other.episodeId)
                && Objects.equals(this.episodeTitle, other.episodeTitle)
                && Objects.equals(this.species, other.species)
                && Objects.equals(this.characters, other.characters);
        }

        @Override
        public String toString() {
            return "PeterDavisonEpisode{" +
                "episodeId='" + episodeId + '\'' +
                ", episodeTitle='" + episodeTitle + '\'' +
                ", species=" + species +
                ", characters=" + characters +
                '}';
        }

        private static <T> List<T> mapIterableColumn(Iterable<String> column, Function<String, T> mapper) {
            return StreamOperations.create(column).map(mapper).collect(toList());
        }
    }

    static class Species implements Comparable<Species> {

        private final String species;

        private Species(String species) {
            this.species = species;
        }

        public static Species species(String species) {
            return new Species(species);
        }

        public String getSpecies() {
            return species;
        }

        @Override
        public int hashCode() {
            return Objects.hash(species);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Species other = (Species) obj;
            return Objects.equals(this.species, other.species);
        }

        @Override
        public String toString() {
            return species;
        }

        @Override
        public int compareTo(Species o) {
            return species.compareTo(o.species);
        }
    }

    static class DoctorWhoCharacter implements Comparable<DoctorWhoCharacter> {

        private final String character;

        public DoctorWhoCharacter(String character) {
            this.character = character;
        }

        public static DoctorWhoCharacter character(String character) {
            return new DoctorWhoCharacter(character);
        }

        public String getCharacter() {
            return character;
        }

        @Override
        public int hashCode() {
            return Objects.hash(character);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final DoctorWhoCharacter other = (DoctorWhoCharacter) obj;
            return Objects.equals(this.character, other.character);
        }

        @Override
        public String toString() {
            return character;
        }

        @Override
        public int compareTo(DoctorWhoCharacter o) {
            return character.compareTo(o.character);
        }
    }


}
