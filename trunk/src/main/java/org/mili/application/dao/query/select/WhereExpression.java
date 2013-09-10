package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public class WhereExpression {
    private Field field;
    private String operator;

    public WhereExpression(Field field, String operator) {
        this.field = field;
        this.operator = operator;
    }

    public Field getField() {
        return field;
    }

    public String getOperator() {
        return operator;
    }

    public String toString() {
        return String.format("%s %s ?", Util.fieldName(getField()), getOperator());
    }

}
