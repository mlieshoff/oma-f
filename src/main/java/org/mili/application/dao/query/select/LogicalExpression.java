package org.mili.application.dao.query.select;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 29.07.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class LogicalExpression extends WhereExpression {

    public LogicalExpression(String operator) {
        super(null, operator);
    }

    public String toString() {
        return getOperator();
    }

}
