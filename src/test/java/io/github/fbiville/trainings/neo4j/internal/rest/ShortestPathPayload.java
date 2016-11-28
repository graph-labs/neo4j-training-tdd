package io.github.fbiville.trainings.neo4j.internal.rest;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.Collection;

public class ShortestPathPayload {

    private final Gson gson;

    private ShortestPathPayload() {
        gson = new Gson();
    }

    public static ShortestPathPayload shortestPathPayload() {
        return new ShortestPathPayload();
    }

    public RequestBody create(int destinationNodeId, Collection<RestRelationshipType> relationshipTypes) {
        String contents = contents(destinationNodeId, relationshipTypes);
        return RequestBody.create(MediaType.parse("application/json"), contents);
    }

    private String contents(int destinationNodeId, Collection<RestRelationshipType> relationshipTypes) {
        return String.format(
            "{\"to\":\"http://localhost:7474/db/data/node/%d\",\"max_depth\":10,\"relationships\":%s,\"algorithm\":\"shortestPath\"}",
            destinationNodeId,
            gson.toJson(relationshipTypes)
        );
    }
}
