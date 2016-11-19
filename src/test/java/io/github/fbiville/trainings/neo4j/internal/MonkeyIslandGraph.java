package io.github.fbiville.trainings.neo4j.internal;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

public class MonkeyIslandGraph {

    public static void create(GraphDatabaseService graphDb) {
        try (Transaction transaction = graphDb.beginTx()) {
            Label videoGame = Label.label("Video game");
            Label characters = Label.label("Character");
            Label heroes = Label.label("Hero");
            Label villains = Label.label("Villain");
            Label intruder = Label.label("Intruder");

            Node monkeyIsland = graphDb.createNode(videoGame);
            monkeyIsland.setProperty("title", "Monkey Island");

            RelationshipType loves = RelationshipType.withName("LOVES");
            RelationshipType hates = RelationshipType.withName("HATES");
            RelationshipType fights = RelationshipType.withName("FIGHTS");
            RelationshipType appearsIn = RelationshipType.withName("APPEARS_IN");

            Node guybrush = graphDb.createNode(characters, heroes);
            guybrush.setProperty("name", "Guybrush Threepwood");
            guybrush.setProperty("in_first_opus", true);

            Node elaine = graphDb.createNode(characters, heroes);
            elaine.setProperty("name", "Elaine Marley");
            elaine.setProperty("in_first_opus", true);

            Node leChuck = graphDb.createNode(characters, villains);
            leChuck.setProperty("name", "LeChuck");
            leChuck.setProperty("in_first_opus", true);

            Node largo = graphDb.createNode(characters, villains);
            largo.setProperty("name", "Largo LaGrande");
            largo.setProperty("in_first_opus", false);

            guybrush.createRelationshipTo(monkeyIsland, appearsIn);
            guybrush.createRelationshipTo(elaine, loves);
            guybrush.createRelationshipTo(leChuck, fights);
            guybrush.createRelationshipTo(largo, fights);
            largo.createRelationshipTo(guybrush, hates);
            largo.createRelationshipTo(monkeyIsland, appearsIn);
            leChuck.createRelationshipTo(elaine, loves);
            leChuck.createRelationshipTo(guybrush, hates);
            leChuck.createRelationshipTo(monkeyIsland, appearsIn);
            elaine.createRelationshipTo(leChuck, hates);
            elaine.createRelationshipTo(monkeyIsland, appearsIn);

            Node nobody = graphDb.createNode(Label.label("Nobody"));
            nobody.createRelationshipTo(monkeyIsland, hates);

            Node florent = graphDb.createNode(intruder);
            florent.setProperty("name", "Florent Biville");
            transaction.success();
        }
    }
}
