package io.github.fbiville.trainings.neo4j._2_traversal;

import io.github.fbiville.trainings.neo4j.internal.GraphTests;
import io.github.fbiville.trainings.neo4j.internal.MonkeyIslandGraph;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Traverser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class _2_TraversalTest extends GraphTests {

    @Before
    public void prepare() {
        MonkeyIslandGraph.create(graphDb);
    }

    @Test
    public void should_traverse_hate_relationships() {
        try (Transaction ignored = graphDb.beginTx()) {
            Node elaine = graphDb.findNode(Label.label("Character"), "name", "Elaine Marley");
             /*
              * TODO: start a graph from Elaine to find who she (transitively) hates
              * HINT: You don't need to write a custom evaluator!
              */
            Iterable<Node> nodes = null;
            fail("You should write a traversal to know who Elaine transitively hates");

            assertThat(nodes)
                .hasSize(3)
                .extracting(node -> node.getProperty("name"))
                .containsOnly("Guybrush Threepwood", "LeChuck", "Largo LaGrande");
        }
    }

    @Test
    public void should_determine_whether_florent_and_guybrush_are_connected() {
        try (Transaction ignored = graphDb.beginTx()) {

            Node florent = null/*TODO*/;
            Node guybrush = null /*TODO*/;

            Traverser traverser = graphDb.bidirectionalTraversalDescription()
                // TODO: describe unidirectional traversals for both sides
                // HINT: it's VERY simple
                .traverse(florent, guybrush);

            fail("You should write a bidirectional traversal");

            assertThat(traverser.nodes()).isEmpty();
        }
    }

    @Test
    public void should_use_shortest_path_algorithms_to_find_the_same_result() {
        try (Transaction ignored = graphDb.beginTx()) {
            Node florent = null/* TODO */;
            Node guybrush = null/*TODO*/;
            PathFinder<Path> paths = null/*TODO (you don't have to reimplement an algorithm yourself!) */;
            fail("You should use the implemented shortest path algorithm");

            assertThat(paths.findSinglePath(florent, guybrush)).isNullOrEmpty();
        }
    }
}
