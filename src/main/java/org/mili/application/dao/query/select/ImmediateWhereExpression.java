package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class ImmediateWhereExpression extends WhereExpression {
    private Object value;

    public ImmediateWhereExpression(Field field, String operator, Object value) {
        super(field, operator);
        this.value = value;
    }

    public String toString() {
        return String.format("%s %s %s", Util.fieldName(getField()), getOperator(), Util.getValue(value));
    }

}
