package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class SelectQuery {
    private List<Field> fields = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<WhereExpression> expressions = new ArrayList<>();
    private List<OrderExpression> orderExpressions = new ArrayList<>();
    private String from;


    public void addValue(Object value) {
        values.add(value);
    }

    public void addOrderExpression(OrderExpression expression) {
        orderExpressions.add(expression);
    }

    public void addExpression(WhereExpression expression) {
        expressions.add(expression);
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("select %s from %s", Util.fieldsToList(fields), Util.fieldName(from)));
        for (int i = 0, n = expressions.size(); i < n; i ++) {
            if (i == 0) {
                s.append(" where ");
            }
            s.append(expressions.get(i));
            if (i < n - 1) {
                s.append(" ");
            }
        }
        for (int i = 0, n = orderExpressions.size(); i < n; i ++) {
            if (i == 0) {
                s.append(" order by ");
            }
            s.append(orderExpressions.get(i));
            if (i < n - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }

    public List<Object> getValues() {
        return values;
    }
}
