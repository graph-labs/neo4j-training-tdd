package io.github.fbiville.trainings.neo4j.internal;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.AutoIndexer;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.test.rule.DatabaseRule;
import org.neo4j.test.rule.ImpermanentDatabaseRule;

public class GraphTests {

    @ClassRule
    public static final DatabaseRule graphDb = new ImpermanentDatabaseRule();
    protected static GraphOperations graphOperations;

    @BeforeClass
    public static void prepareAll() {
        graphOperations = new GraphOperations(graphDb);
    }

    @After
    public void cleanUp() {
        removeIndices();
        deleteGraph();
    }

    private void removeIndices() {
        graphDb.executeAndCommit(db -> {
            removeLegacyIndices();
            removeSchema();
        });
    }

    private void deleteGraph() {
        graphDb.executeAndCommit(db -> {
            // usually order matters, relationships should be deleted before nodes
            // because deleted nodes cannot have dangling relationships
            // *however*, we are in the same transaction here, so it does not matter
            StreamOperations.create(db.getAllNodes()).forEach(Node::delete);
            StreamOperations.create(db.getAllRelationships()).forEach(Relationship::delete);
        });
    }

    private void removeLegacyIndices() {
        removeAutomaticIndices();
        removeManualIndices();
    }

    private void removeManualIndices() {
        IndexManager indexManager = graphDb.index();

        StreamOperations.create(indexManager.nodeIndexNames())
            .filter(name -> !name.equals("node_auto_index"))
            .forEach(indexName -> {
                indexManager.forNodes(indexName).delete();
            });

        StreamOperations.create(indexManager.relationshipIndexNames())
            .filter(name -> !name.equals("relationship_auto_index"))
            .forEach(indexName -> {
                indexManager.forRelationships(indexName).delete();
            });
    }

    private void removeAutomaticIndices() {
        IndexManager indexManager = graphDb.index();
        AutoIndexer<Node> nodeAutoIndexer = indexManager.getNodeAutoIndexer();
        StreamOperations.create(nodeAutoIndexer.getAutoIndexedProperties())
            .forEach(nodeAutoIndexer::stopAutoIndexingProperty);
        nodeAutoIndexer.setEnabled(false);

        AutoIndexer<Relationship> relationshipAutoIndexer = indexManager.getRelationshipAutoIndexer();
        StreamOperations.create(relationshipAutoIndexer.getAutoIndexedProperties())
            .forEach(relationshipAutoIndexer::stopAutoIndexingProperty);
        relationshipAutoIndexer.setEnabled(false);
    }

    private void removeSchema() {
        StreamOperations.create(graphDb.schema().getConstraints())
            .forEach(ConstraintDefinition::drop);
        StreamOperations.create(graphDb.schema().getIndexes())
            .forEach(IndexDefinition::drop);
    }
}
