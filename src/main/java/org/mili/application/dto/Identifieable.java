package org.mili.application.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 18.07.13
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class Identifieable extends Attributeable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Field, Object> valueMap() {
        Map<Field, Object> map = new HashMap<>();
        map.put(Fields.ID, getId());
        return map;
    }

    public Map<Field, Object> valueMapWithoutId() {
        return new HashMap<>();
    }

    public abstract Map<Field, Object> getUniqueFields();

    public static enum Fields implements Field {
        ID;
    }
}
