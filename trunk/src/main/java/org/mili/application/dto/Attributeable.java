package org.mili.application.dto;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 18.07.13
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class Attributeable {
    public abstract Map<Field, Object> valueMap();

    public abstract Map<Field, Object> valueMapWithoutId();

    public abstract Map<Field, Object> getUniqueFields();

}
