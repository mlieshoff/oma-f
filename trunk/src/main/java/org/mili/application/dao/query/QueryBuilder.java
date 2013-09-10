package org.mili.application.dao.query;

import org.mili.application.dao.query.select.SelectBuilder;
import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 09:46
 * To change this template use File | Settings | File Templates.
 */
public class QueryBuilder {

    private Query query = new Query();

    public static QueryBuilder createQueryBuilder() {
        return new QueryBuilder();
    }

    public Query build() {
        return query;
    }

    public SelectBuilder select() {
        SelectBuilder selectBuilder = new SelectBuilder();
        return selectBuilder;
    }

    public static void main(String[] args) {
        System.out.println(createQueryBuilder()
                .select()
                .fields(Fields.ID)
                .from("test")
                .where()
                .eq(Fields.ID, 4611)
                .and()
                .like(Fields.NAME, "abbas")
                .or()
                .orderBy()
                .asc(Fields.ID)
                .desc(Fields.NAME)
                .build().toString());
    }

    public static enum Fields implements Field {
        NAME,
        ID;
    }



}
