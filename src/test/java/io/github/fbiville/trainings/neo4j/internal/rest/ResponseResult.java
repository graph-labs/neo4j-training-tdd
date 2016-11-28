package io.github.fbiville.trainings.neo4j.internal.rest;

import java.util.Collection;
import java.util.Map;

public class ResponseResult {

    private Collection<String> columns;
    private Collection<ResponseData> data;
    private Collection<Transaction> transaction;
    private Collection<Map<String, Object>> stats;

    public Collection<String> getColumns() {
        return columns;
    }

    public void setColumns(Collection<String> columns) {
        this.columns = columns;
    }

    public Collection<ResponseData> getData() {
        return data;
    }

    public void setData(Collection<ResponseData> data) {
        this.data = data;
    }

    public Collection<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(Collection<Transaction> transaction) {
        this.transaction = transaction;
    }

    public Collection<Map<String, Object>> getStats() {
        return stats;
    }

    public void setStats(Collection<Map<String, Object>> stats) {
        this.stats = stats;
    }
}
