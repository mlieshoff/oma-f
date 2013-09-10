package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public class InWhereExpression extends WhereExpression {
    private final Object[] values;

    public InWhereExpression(Field field, List<Object> values) {
        super(field, "in");
        this.values = values.toArray();
    }

    public String toString() {
        return String.format("%s %s (%s)", Util.fieldName(getField()), getOperator(), Util.getValueList(values));
    }

}
