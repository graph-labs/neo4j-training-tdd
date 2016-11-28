package io.github.fbiville.trainings.neo4j.internal.rest;

import java.util.ArrayList;
import java.util.Collection;

public class TransactionalRequestPayload {

    private final Collection<CypherStatement> statements = new ArrayList<>();

    private TransactionalRequestPayload() {

    }

    public static TransactionalRequestPayload transactionalPayload() {
        return new TransactionalRequestPayload();
    }

    public TransactionalRequestPayload add(CypherStatement statement) {
        statements.add(statement);
        return this;
    }

    public Collection<CypherStatement> getStatements() {
        return statements;
    }
}
