package io.github.fbiville.trainings.neo4j.internal.rest;

import java.util.Collection;
import java.util.Map;

public class ResponseData {

    private Collection<Object> row;
    private Collection<Map<String,Object>> meta;

    public Collection<Object> getRow() {
        return row;
    }

    public void setRow(Collection<Object> row) {
        this.row = row;
    }

    public Collection<Map<String, Object>> getMeta() {
        return meta;
    }

    public void setMeta(Collection<Map<String, Object>> meta) {
        this.meta = meta;
    }
}
