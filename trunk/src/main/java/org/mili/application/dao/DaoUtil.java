package org.mili.application.dao;

import org.mili.application.dao.query.QueryBuilder;
import org.mili.application.dao.query.select.SelectBuilder;
import org.mili.application.dao.query.select.SelectQuery;
import org.mili.application.dto.Field;
import org.mili.application.dto.Identifieable;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 17.07.13
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class DaoUtil {

    public static String insertion(Field[] fields, String table) {
        StringBuilder attribs = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < fields.length; i ++) {
            attribs.append(String.format("`%s`", fields[i]));
            values.append("?");
            if (i < fields.length - 1) {
                attribs.append(", ");
                values.append(", ");
            }
        }
        return String.format("insert into `%s` (%s) values(%s)", table, attribs, values);
    }

    public static String update(Field[] fields, String table, String where) {
        StringBuilder attribs = new StringBuilder();
        for (int i = 0; i < fields.length; i ++) {
            attribs.append(String.format("`%s`=?", fields[i]));
            if (i < fields.length - 1) {
                attribs.append(", ");
            }
        }
        return String.format("update `%s` set %s where %s", table, attribs, where);
    }

    public static SelectQuery unique(Field[] uniqueFields, String table, Object[] objects) {
        SelectBuilder builder = QueryBuilder.createQueryBuilder().select();
        builder = builder.fields(Identifieable.Fields.ID).from(table);
        for (int i = 0; i < uniqueFields.length; i ++) {
            Field field = uniqueFields[i];
            builder = builder.eq(field, objects[i]);
            if (i < uniqueFields.length - 1) {
                builder = builder.and();
            }
        }
        return builder.build();
    }

    public static String delete(String table) {
        return String.format("delete from `%s` where `id`=?", table);
    }

    public static String delete(String[] fields, String table) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < fields.length; i ++) {
            s.append(String.format("`%s`=?", fields[i]));
            if (i < fields.length - 1) {
                s.append(" and ");
            }
        }
        return String.format("delete from `%s` where %s", table, s);
    }

    public static String field(String name) {
        return String.format("`%s`", name);
    }

}
