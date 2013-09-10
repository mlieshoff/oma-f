package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Expression;
import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class OrderExpression extends Expression {
    private String operator;
    private Field field;

    public OrderExpression(String operator, Field field) {
        this.operator = operator;
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return String.format("%s %s", Util.fieldName(field), operator);
    }
}
