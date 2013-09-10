package org.mili.application.dao.query.select;

import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 09:49
 * To change this template use File | Settings | File Templates.
 */
public class Select {
    private final Field[] fields;
    private final String from;

    public Select(String from, Field[] fields) {
        this.fields = fields;
        this.from = from;
    }
}
