package io.github.fbiville.trainings.neo4j.internal.rest;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ShortestPathPayload {

    public static ShortestPathPayload shortestPathPayload() {
        return new ShortestPathPayload();
    }

    public RequestBody create(int destinationNodeId) {
        String contents = contents(destinationNodeId);
        return RequestBody.create(MediaType.parse("application/json"), contents);
    }

    private String contents(int destinationNodeId) {
        return String.format(
            "{\"to\":\"http://localhost:7474/db/data/node/%d\",\"max_depth\":10,\"algorithm\":\"shortestPath\"}",
            destinationNodeId
        );
    }
}
