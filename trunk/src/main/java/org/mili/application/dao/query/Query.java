package org.mili.application.dao.query;


import org.mili.application.dao.query.select.Select;
import org.mili.application.dto.Field;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 09:47
 * To change this template use File | Settings | File Templates.
 */
public class Query {
    private Select select;

    public void setSelect(String from, Field[] fields) {
        select = new Select(from, fields);
    }
}
