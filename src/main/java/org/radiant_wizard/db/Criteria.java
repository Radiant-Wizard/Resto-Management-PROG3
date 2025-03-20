package org.radiant_wizard.db;

import lombok.Getter;
import org.radiant_wizard.Entity.Enum.LogicalOperator;

@Getter
public class Criteria {
    private String columnName;
    private Object columnValue;
    private Object secondValue;
    private String operator;
    private LogicalOperator logicalOperator;

    public Criteria(String columnName, Object columnValue, Object secondValue, String operator, LogicalOperator logicalOperator) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.secondValue = secondValue;
        this.operator = operator;
        this.logicalOperator = logicalOperator;
    }

    public Criteria(String columnName, Object columnValue, String operator, LogicalOperator logicalOperator) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.operator = operator;
        this.logicalOperator = logicalOperator;
    }
}
