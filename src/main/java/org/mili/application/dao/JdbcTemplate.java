package org.mili.application.dao;

import org.mili.application.dao.query.select.SelectQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcTemplate {
	private ConnectionPool connectionPool = new ConnectionPool();
	private Map<String, PreparedStatement> cache = new ConcurrentHashMap<String, PreparedStatement>();

    protected <T> List<T> query(RowTransformer<T> rowTransformer, SelectQuery selectQuery) throws SQLException {
        return query(rowTransformer, selectQuery.toString(), selectQuery.getValues().toArray());
    }

    protected <T> List<T> query(RowTransformer <T> rowTransformer, String sql, Object... objects) throws SQLException {
		sql = normalize(sql);
		System.out.println(connectionPool.get() + " - query: " + sql);
		PreparedStatement preparedStatement = cache.get(sql);
		if (preparedStatement == null) {
			preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
			cache.put(sql, preparedStatement);
		}
		fillWithObjects(preparedStatement, objects);
		List<T> list = new ArrayList<>();
		ResultSet result = preparedStatement.executeQuery();
		while(result.next()) {
			list.add(rowTransformer.transform(result));
		}
        System.out.println(connectionPool.get() + " - size: " + list.size());
		return list;
	}

	private void fillWithObjects(PreparedStatement preparedStatement, Object[] objects) throws SQLException {
		if (objects != null) {
			for (int i = 0; i < objects.length; i ++) {
                Object o = objects[i];
                if (o instanceof String) {
                    o = o.toString().trim();
                }
                preparedStatement.setObject(i + 1, o);
				System.out.println("    " + (i + 1) + " = " + o);
			}
		}
	}

	protected <T> T querySingle(RowTransformer<T> rowTransformer, SelectQuery selectQuery) throws SQLException {
		return querySingle(rowTransformer, selectQuery.toString(), selectQuery.getValues().toArray());
	}

    protected <T> T querySingle(RowTransformer<T> rowTransformer, String sql, Object... objects) throws SQLException {
        List<T> list = query(rowTransformer, sql, objects);
        int size = list.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return list.get(0);
        } else {
            throw new IllegalStateException("more than 1 result found[" + size + "]!");
        }
    }

    private String normalize(String sql) {
		sql = sql.replace("\r\n", "").replace("\n", "");
		sql = sql.replaceAll("\\s\\s+"," ");
		return sql;
	}

	protected int update(String sql, Object... objects) throws SQLException {
		sql = normalize(sql);
		System.out.println(connectionPool.get() + " - update: " + sql);
		PreparedStatement preparedStatement = cache.get(sql);
		if (preparedStatement == null) {
			preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
			cache.put(sql, preparedStatement);
		}
		fillWithObjects(preparedStatement, objects);
		return preparedStatement.executeUpdate();
	}

	protected boolean execute(String sql, Object... objects) throws SQLException {
		sql = normalize(sql);
		System.out.println(connectionPool.get() + " - execute: " + sql);
		PreparedStatement preparedStatement = cache.get(sql);
		if (preparedStatement == null) {
			preparedStatement = connectionPool.get().getConnection().prepareStatement(sql);
			cache.put(sql, preparedStatement);
		}
		if (objects != null) {
			for (int i = 0; i < objects.length; i ++) {
                Object o = objects[i];
                if (o instanceof String) {
                    o = o.toString().trim();
                }
				preparedStatement.setObject(i + 1, o);
			}
		}
		return preparedStatement.execute();
	}

    protected long getTransactionCreationTime() throws SQLException {
        return connectionPool.get().getCreationTime();
    }
}
