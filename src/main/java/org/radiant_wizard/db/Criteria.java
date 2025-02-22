package org.radiant_wizard.db;

import lombok.Getter;

@Getter
public class Criteria {
    private String columnName;
    private Object columnValue;

    public Criteria(Object columnValue, String columnName) {
        this.columnValue = columnValue;
        this.columnName = columnName;
    }
}
