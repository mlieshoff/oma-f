package org.mili.application.dto;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 01.08.13
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public interface Archivable {

    public static String PREFIX = "revision_";

    public static enum Fields implements Field {
        MODIFIEDAT,
        MODIFICATIONTYPE;
    }

}
