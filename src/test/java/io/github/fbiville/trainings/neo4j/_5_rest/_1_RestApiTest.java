package io.github.fbiville.trainings.neo4j._5_rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.fbiville.trainings.neo4j.internal.rest.Neo4jBasicAuthenticator;
import io.github.fbiville.trainings.neo4j.internal.rest.Neo4jRestClient;
import io.github.fbiville.trainings.neo4j.internal.rest.ResponseData;
import io.github.fbiville.trainings.neo4j.internal.rest.ResponseResult;
import io.github.fbiville.trainings.neo4j.internal.rest.TransactionalResponse;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static io.github.fbiville.trainings.neo4j.internal.rest.CypherStatement.cypher;
import static io.github.fbiville.trainings.neo4j.internal.rest.ShortestPathPayload.shortestPathPayload;
import static io.github.fbiville.trainings.neo4j.internal.rest.TransactionalRequestPayload.transactionalPayload;
import static org.assertj.core.api.Assertions.assertThat;

public class _1_RestApiTest {

    private final Neo4jRestClient restClient = new Neo4jRestClient(new Neo4jBasicAuthenticator());
    private final Gson gson = new Gson();

    @Before
    public void prepare() throws IOException {
        checkUrl("http://localhost:7474/db/data/");
    }

    @Test
    public void should_make_sure_there_are_Doctor_Who_nodes() {
        String query = "MATCH (n) RETURN COUNT(n) AS count";
        String transactionalEndpointUrl = "http://localhost:7474/db/data/transaction/commit";

        TransactionalResponse response = restClient.postCypher(transactionalEndpointUrl, transactionalPayload().add(cypher(query)));

        assertThat(response.getErrors()).isEmpty();
        Collection<ResponseResult> results = response.getResults();
        assertThatReturnedColumnAre(results, "count");
        assertThat((Double) singleRow(results))
            .withFailMessage("No Doctor Who node found. Have you imported the graph as specified by the instructions?")
            .isGreaterThan(0);
    }

    @Test
    public void should_find_length_of_the_shortest_path_between_sarah_jane_smith_and_skaro() throws IOException {
        int sarahNodeId = getSarahJaneSmithNodeId();
        int skaroId = getSkaroNodeId();
        String shortestPathUrl = "http://localhost:7474/db/data/node/" + sarahNodeId + "/path";

        Response response = restClient.post(shortestPathUrl, shortestPathPayload().create(skaroId));
        Map<String, Object> responseData = asDictionary(response.body().string());
        assertThat(asInt(responseData.get("length"))).isEqualTo(3);
    }

    private void checkUrl(String url) throws IOException {
        Response response = restClient.get(url);
        assertThat(response.isSuccessful())
            .withFailMessage("Expected successful HTTP response, got %d.\nBody:%s\nHave you started your Neo4j instance", response.code(), response.body().string())
            .isTrue();
    }

    private void assertThatReturnedColumnAre(Collection<ResponseResult> results, String... columns) {
        assertThat(results).hasSize(1).flatExtracting(ResponseResult::getColumns).containsOnly(columns);
    }

    private Object singleRow(Collection<ResponseResult> results) {
        Collection<ResponseData> data = results.iterator().next().getData();
        assertThat(data).hasSize(1);
        Collection<Object> row = data.iterator().next().getRow();
        assertThat(row).hasSize(1);
        return row.iterator().next();
    }

    private int getSarahJaneSmithNodeId() {
        TransactionalResponse response = restClient.postCypher("http://localhost:7474/db/data/transaction/commit", transactionalPayload().add(cypher("MATCH (n:Character {character:'Sarah Jane Smith'}) RETURN ID(n)")));
        return asInt(singleRow(response.getResults()));
    }

    private int getSkaroNodeId() {
        TransactionalResponse response2 = restClient.postCypher("http://localhost:7474/db/data/transaction/commit", transactionalPayload().add(cypher("MATCH (n:Planet {planet:'Skaro'}) RETURN ID(n)")));
        return asInt(singleRow(response2.getResults()));
    }

    private static int asInt(Object number) {
        if (number instanceof Number) {
            return ((Number) number).intValue();
        }
        throw new IllegalArgumentException("Not a number: " + number);
    }

    private Map<String, Object> asDictionary(String responseContents) {
        return gson.fromJson(responseContents, new TypeToken<Map<String, Object>>() {}.getType());
    }

}
