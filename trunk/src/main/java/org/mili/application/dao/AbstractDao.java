package org.mili.application.dao;

import org.apache.commons.lang.ArrayUtils;
import org.mili.application.dao.query.QueryBuilder;
import org.mili.application.dao.query.select.SelectQuery;
import org.mili.application.dto.Archivable;
import org.mili.application.dto.Attributeable;
import org.mili.application.dto.Field;
import org.mili.application.dto.Identifieable;
import org.mili.application.util.Lambda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class AbstractDao<T> extends Dao {
    private static final Field[] ARCHIVEDFIELDS = (Field[]) ArrayUtils.addAll(new Field[]{Identifieable.Fields.ID},
            Archivable.Fields.values());
    private static final Field[] NM_ARCHIVEDFIELDS = (Field[]) ArrayUtils.addAll(new Field[]{},
            Archivable.Fields.values());

    protected Field[] fields;
    protected Field[] uniqueFields;
    protected String table;

    protected AbstractDao(Field[] uniqueFields, Field[] fields, String table) {
        this.fields = fields;
        this.uniqueFields = uniqueFields;
        this.table = table;
    }

    public Integer lastInsertId() throws DaoException {
        return doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                return querySingle(new RowTransformer<Integer>() {
                    @Override
                    public Integer transform(ResultSet resultSet) throws SQLException {
                        return resultSet.getInt(1);
                    }
                }, "select LAST_INSERT_ID()");
            }
        });
    }

    public List<T> read(final RowTransformer<T> rowTransformer) throws DaoException {
        return read(rowTransformer,
                QueryBuilder.createQueryBuilder()
                        .select()
                        .fields(Identifieable.Fields.ID, fields)
                        .from(table)
                        .build());
    }

    public List<T> read(final RowTransformer<T> rowTransformer, final SelectQuery selectQuery) throws DaoException {
        return doInDao(new Lambda<List<T>>() {
            @Override
            public List<T> exec(Object... params) throws SQLException {
                return query(rowTransformer, selectQuery);
            }
        });
    }

    public T readSingle(final RowTransformer<T> rowTransformer) throws DaoException {
        return readSingle(rowTransformer,
                QueryBuilder.createQueryBuilder()
                        .select()
                        .fields(Identifieable.Fields.ID, fields)
                        .from(table)
                        .build());
    }

    public T readSingle(final RowTransformer<T> rowTransformer, final SelectQuery selectQuery) throws DaoException {
        return doInDao(new Lambda<T>() {
            @Override
            public T exec(Object... params) throws SQLException {
                return querySingle(rowTransformer, selectQuery);
            }
        });
    }

    public boolean exists(final Identifieable object) throws DaoException {
        return id(object) > 0;
    }

    public int id(final Identifieable object) throws DaoException {
        return doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                Integer id = querySingle(new RowTransformer<Integer>() {
                    @Override
                    public Integer transform(ResultSet resultSet) throws SQLException {
                        return resultSet.getInt(Identifieable.Fields.ID.name());
                    }
                }, DaoUtil.unique(uniqueFields, table, toArray(uniqueFields, object.getUniqueFields())));
                return id != null ? id : 0;
            }
        });
    }

    public ModificationResult createOrUpdate(final Identifieable object) throws DaoException {
        if (object.getId() > 0) {
            int rows = doInDao(new Lambda<Integer>() {
                @Override
                public Integer exec(Object... params) throws SQLException {
                    return update(DaoUtil.update(fields, table, "`id`=?"), toArray(fields, object.valueMap()));
                }
            });
            archive(object.getId(), object, MofificationType.UPDATE);
            return new ModificationResult(ModificationResult.Type.UPDATE, rows == 1, rows, object.getId());
        } else {
            final int id = id(object);
            if (id > 0) {
                int rows = doInDao(new Lambda<Integer>() {
                    @Override
                    public Integer exec(Object... params) throws SQLException {
                        Map<Field, Object> map = object.valueMapWithoutId();
                        map.put(Identifieable.Fields.ID, id);
                        return update(DaoUtil.update(fields, table, "`id`=?"), toArray(fields, map));
                    }
                });
                archive(id, object, MofificationType.UPDATE);
                return new ModificationResult(ModificationResult.Type.UPDATE, rows == 1, rows, id);
            } else {
                int rows = doInDao(new Lambda<Integer>() {
                    @Override
                    public Integer exec(Object... params) throws SQLException {
                        return update(DaoUtil.insertion(fields, table), toArray(fields, object.valueMapWithoutId()));
                    }
                });
                ModificationResult modificationResult = new ModificationResult(ModificationResult.Type.INSERT,
                        rows == 1, rows, lastInsertId());
                archive(modificationResult.getId(), object, MofificationType.CREATE);
                return modificationResult;
            }
        }
    }

    private int archive(final int id, final Identifieable object, final MofificationType mofificationType)
            throws DaoException {
        if (object instanceof Archivable) {
            if (object instanceof Identifieable) {
                return doInDao(new Lambda<Integer>() {
                    @Override
                    public Integer exec(Object... params) throws SQLException {
                        Field[] fieldCopy = (Field[]) ArrayUtils.addAll(ARCHIVEDFIELDS, fields);
                        Object[] values = ArrayUtils.addAll(new Object[]{id, new Timestamp(getTransactionCreationTime()),
                                mofificationType.getCode()}, toArray(fields, object.valueMapWithoutId()));
                        return update(DaoUtil.insertion(fieldCopy, Archivable.PREFIX + table), values);
                    }
                });
            }
        }
        return -1;
    }

    protected int archive(final Attributeable object, final MofificationType mofificationType)
            throws DaoException {
        if (object instanceof Archivable) {
            return doInDao(new Lambda<Integer>() {
                @Override
                public Integer exec(Object... params) throws SQLException {
                    Field[] fieldCopy = (Field[]) ArrayUtils.addAll(NM_ARCHIVEDFIELDS, fields);
                    Object[] values = ArrayUtils.addAll(new Object[]{new Timestamp(getTransactionCreationTime()),
                            mofificationType.getCode()}, toArray(fields, object.valueMapWithoutId()));
                    return update(DaoUtil.insertion(fieldCopy, Archivable.PREFIX + table), values);
                }
            });
        }
        return -1;
    }

    public ModificationResult delete(final Identifieable object) throws DaoException {
        int rowsAffected = doInDao(new Lambda<Integer>() {
            @Override
            public Integer exec(Object... params) throws SQLException {
                return update(DaoUtil.delete(table), object.getId());
            }
        });
        archive(object.getId(), object, MofificationType.DELETE);
        return new ModificationResult(ModificationResult.Type.DELETE, rowsAffected == 1, rowsAffected, object.getId());
    }

    private Object[] toArray(Field[] fields, Map<Field, Object> map) {
        boolean containsId = map.containsKey(Identifieable.Fields.ID);
        int size = fields.length + (containsId ? 1 : 0);
        Object[] array = new Object[size];
        for (int i = 0; i < fields.length; i ++) {
            array[i] = map.get(fields[i]);
        }
        if (containsId) {
            array[size - 1] = map.get(Identifieable.Fields.ID);
        }
        return array;
    }

    public Field[] getFields() {
        return fields;
    }
}
