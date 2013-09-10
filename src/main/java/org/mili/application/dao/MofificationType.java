package org.mili.application.dao;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 01.08.13
 * Time: 20:44
 * To change this template use File | Settings | File Templates.
 */
public enum MofificationType {
    CREATE(1),
    UPDATE(2),
    DELETE(3);

    private final int code;

    MofificationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
