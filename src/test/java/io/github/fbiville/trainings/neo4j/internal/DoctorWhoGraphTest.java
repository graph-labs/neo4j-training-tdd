package io.github.fbiville.trainings.neo4j.internal;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DoctorWhoGraphTest {

    @Test
    public void turns_multiline_statement_to_single_line() throws Exception {
        String cql =
                "CREATE (_1:Foo {foo:'fighters'})\n" +
                "CREATE (_2:Bar {bar:'mitzvah'})\n" +
                "CREATE (_1)-[:IS_AS_BAD_AS_A_NAME_AS {like:'really'}]->(_2);";

        List<String> statements = DoctorWhoGraph.toStandaloneStatements(Arrays.asList(cql.split("\n")));

        assertThat(statements)
                .containsExactly(
                        "MERGE (start:Foo {foo:'fighters'}) " +
                        "MERGE (end:Bar {bar:'mitzvah'}) " +
                        "CREATE (start)-[:IS_AS_BAD_AS_A_NAME_AS {like:'really'}]->(end)");
    }
}
