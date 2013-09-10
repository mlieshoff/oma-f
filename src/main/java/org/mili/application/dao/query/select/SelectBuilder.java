package org.mili.application.dao.query.select;

import org.mili.application.dao.query.Util;
import org.mili.application.dto.Field;
import org.mili.application.dto.NativeField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 09:46
 * To change this template use File | Settings | File Templates.
 */
public class SelectBuilder {
    private SelectQuery query = new SelectQuery();

    static enum State {
        NONE,
        SELECT,
        WHERE;
    }

    private State state = State.NONE;

    public SelectBuilder() {
        state = State.SELECT;
    }

    public SelectQuery build() {
        return query;
    }

    public SelectBuilder from(String table) {
        query.setFrom(table);
        return this;
    }

    public SelectBuilder fields(Field field, Field[] fields) {
        query.addField(field);
        for (Field f : fields) {
            query.addField(f);
        }
        return this;
    }

    public SelectBuilder fields(Field... fields) {
        for (Field field : fields) {
            query.addField(field);
        }
        return this;
    }

    public SelectBuilder where() {
        state = State.WHERE;
        return this;
    }

    public SelectBuilder max(final Field field) {
        if (state == State.SELECT) {
            query.addField(new NativeField() {
                @Override
                public String name() {
                    return String.format("max(%s)", Util.fieldName(field.name()));
                }

                @Override
                public String as() {
                    return String.format("max%s", field.name());
                }
            });
        }
        return this;
    }

    public SelectBuilder and() {
        query.addExpression(new LogicalExpression("and"));
        return this;
    }

    public SelectBuilder or() {
        query.addExpression(new LogicalExpression("or"));
        return this;
    }

    public SelectBuilder ieq(Field field, Object value) {
        query.addExpression(new ImmediateWhereExpression(field, "=", value));
        return this;
    }

    public SelectBuilder eq(Field field, Object value) {
        query.addValue(value);
        query.addExpression(new WhereExpression(field, "="));
        return this;
    }

    public SelectBuilder neq(Field field, Object value) {
        query.addValue(value);
        query.addExpression(new WhereExpression(field, "!="));
        return this;
    }

    public SelectBuilder like(Field field, Object value) {
        query.addValue(value);
        query.addExpression(new WhereExpression(field, "like"));
        return this;
    }

    public SelectBuilder in(Field field, List values) {
        query.addExpression(new InWhereExpression(field, values));
        return this;
    }

    public SelectBuilder orderBy() {
        return this;
    }

    public SelectBuilder asc(Field field) {
        query.addOrderExpression(new OrderExpression("asc", field));
        return this;
    }

    public SelectBuilder desc(Field field) {
        query.addOrderExpression(new OrderExpression("desc", field));
        return this;
    }

}
