package io.github.fbiville.trainings.neo4j.internal.rest;

public class CypherStatement {
    private final String statement;

    private CypherStatement(String statement) {
        this.statement = statement;
    }

    public static CypherStatement cypher(String statement) {
        return new CypherStatement(statement);
    }

    public String getStatement() {
        return statement;
    }
}
