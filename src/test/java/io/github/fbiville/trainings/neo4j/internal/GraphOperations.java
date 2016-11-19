package io.github.fbiville.trainings.neo4j.internal;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GraphOperations {

    private final GraphDatabaseService graphDb;

    public GraphOperations(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    public Node getSingleNode() {
        Iterable<Node> allNodes = getAllNodes();
        assertThat(allNodes)
            .withFailMessage("Expected database to contain only 1 node")
            .hasSize(1);
        return allNodes.iterator().next();
    }

    public Iterable<Node> getAllNodes() {
        return graphDb.getAllNodes();
    }

    public Relationship getSingleRelationship() {
        Iterable<Relationship> allRelationships = getAllRelationships();
        assertThat(allRelationships)
            .withFailMessage("Expected database to contain only 1 relationship")
            .hasSize(1);
        return allRelationships.iterator().next();
    }

    public Iterable<Relationship> getAllRelationships() {
        return graphDb.getAllRelationships();
    }

    public Iterable<String> getLabelNames(Node node) {
        return StreamOperations.create(node.getLabels())
                .map(Label::name)
                .collect(Collectors.toList());
    }

    public String getSingleLabelName(Node node) {
        Iterable<Label> labels = node.getLabels();
        assertThat(labels)
            .withFailMessage("Expected node to define only 1 label")
            .hasSize(1);
        return labels.iterator().next().name();
    }
}
