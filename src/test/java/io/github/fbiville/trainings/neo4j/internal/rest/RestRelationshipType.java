package io.github.fbiville.trainings.neo4j.internal.rest;

public class RestRelationshipType {

    private final String type;

    private RestRelationshipType(String type) {
        this.type = type;
    }

    public static RestRelationshipType relationshipType(String type) {
        return new RestRelationshipType(type);
    }

    public String getType() {
        return type;
    }
}
