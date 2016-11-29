package io.github.fbiville.trainings.neo4j._2_traversal;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.MonkeyIslandGraph;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.BranchOrderingPolicies;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.Traverser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.neo4j.graphalgo.GraphAlgoFactory.shortestPath;

public class _2_TraversalTest extends GraphTests {

    @Before
    public void prepare() {
        MonkeyIslandGraph.create(graphDb);
    }

    @Test
    public void should_traverse_hate_relationships() {
        try (Transaction ignored = graphDb.beginTx()) {
            Node elaine = graphDb.findNode(Label.label("Character"), "name", "Elaine Marley");
            Iterable<Node> nodes = graphDb.traversalDescription()
                    .relationships(RelationshipType.withName("HATES"))
                    .evaluator(Evaluators.excludeStartPosition())
                    .traverse(elaine)
                    .nodes();

            assertThat(nodes)
                    .hasSize(3)
                    .extracting(node -> node.getProperty("name"))
                    .containsOnly("Guybrush Threepwood", "LeChuck", "Largo LaGrande");
        }
    }

    @Test
    public void should_determine_whether_florent_and_guybrush_are_connected() {
        try (Transaction ignored = graphDb.beginTx()) {

            Node florent = graphDb.findNode(Label.label("Intruder"), "name", "Florent Biville");
            Node guybrush = graphDb.findNode(Label.label("Character"), "name", "Guybrush Threepwood");

            Traverser traverser = graphDb.bidirectionalTraversalDescription()
                    .startSide(graphDb.traversalDescription())
                    .endSide(graphDb.traversalDescription())
                    .traverse(florent, guybrush);

            assertThat(traverser.nodes()).isEmpty();
        }
    }

    @Test
    public void should_use_shortest_path_algorithms_to_find_the_same_result() {
        try (Transaction ignored = graphDb.beginTx()) {
            Node florent = graphDb.findNode(Label.label("Intruder"), "name", "Florent Biville");
            Node guybrush = graphDb.findNode(Label.label("Character"), "name", "Guybrush Threepwood");
            PathFinder<Path> paths = shortestPath(PathExpanders.allTypesAndDirections(), 42);

            assertThat(paths.findSinglePath(florent, guybrush)).isNullOrEmpty();
        }
    }
}
