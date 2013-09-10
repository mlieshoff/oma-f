package org.mili.application.dao.query;

import org.mili.application.dto.Field;
import org.mili.application.dto.NativeField;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static String fieldName(Field field) {
        if (field instanceof NativeField) {
            return field.name();
        } else {
            return fieldName(field.name());
        }
    }

    public static String fieldName(String s) {
        return String.format("`%s`", s);
    }

    public static String fieldAs(Field field) {
        if (field instanceof NativeField) {
            NativeField nativeField = (NativeField) field;
            return String.format("'%s'", nativeField.as());
        } else {
            return String.format("'%s'", field.name());
        }
    }

    public static String getValue(Object value) {
        return value instanceof String ? String.format("'%s'", value) : value.toString();
    }

    public static String fieldsToList(List<Field> fields) {
        StringBuilder s = new StringBuilder();
        for (int i = 0, n = fields.size(); i < n; i ++) {
            Field field = fields.get(i);
            s.append(String.format("%s as %s", fieldName(fields.get(i)), fieldAs(fields.get(i))));
            if (i < n - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }

    public static String getValueList(Object[] values) {
        StringBuilder s = new StringBuilder();
        for (int i = 0, n = values.length; i < n; i ++) {
            s.append(Util.getValue(values[i]));
            if (i < n - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }
}
