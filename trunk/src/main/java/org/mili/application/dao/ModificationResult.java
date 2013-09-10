package org.mili.application.dao;

/**
 */
public class ModificationResult {

    public static enum Type {
        UPDATE,
        INSERT,
        DELETE;
    }

    private Type type;
    private boolean succes;
    private int rowsAffected;
    private int id;

    public ModificationResult(Type type, boolean succes, int rowsAffected, int id) {
        this.type = type;
        this.succes = succes;
        this.rowsAffected = rowsAffected;
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public boolean isSucces() {
        return succes;
    }

    public int getRowsAffected() {
        return rowsAffected;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ModificationResult{" +
                "type=" + type +
                ", succes=" + succes +
                ", rowsAffected=" + rowsAffected +
                ", id=" + id +
                '}';
    }
}
