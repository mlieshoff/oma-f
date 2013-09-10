package org.mili.application.dao;

import org.mili.application.dao.Dao;
import org.mili.application.dao.DaoException;
import org.mili.application.dao.RowTransformer;
import org.mili.application.dao.query.QueryBuilder;
import org.mili.application.dto.Field;
import org.mili.application.dto.Identifieable;
import org.mili.application.util.Lambda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SchemaVersionDao extends Dao {
    private boolean mustUpdate = false;

    public static final String TABLE = "schemaversion";

    public static enum Fields implements Field {
        MAXVERSION,
        VERSION;
    }

	public void init(final boolean dropAndCreate) throws DaoException {
		doInDao(new Lambda<Void>() {
			@Override
			public Void exec(Object... params) throws SQLException {
                try {
                    querySingle(new RowTransformer<Object>() {
                        @Override
                        public Object transform(ResultSet resultSet) throws SQLException {
                            return null;
                        }
                    }, QueryBuilder.createQueryBuilder()
                            .select()
                            .max(Identifieable.Fields.ID)
                            .from(TABLE)
                            .build());
                } catch (SQLException e) {
                    mustUpdate = true;
                }
                if (mustUpdate || dropAndCreate) {
                    update("drop table if exists schemaversion;");
                    update("CREATE TABLE IF NOT EXISTS schemaversion("
                            + "id int NOT NULL AUTO_INCREMENT, "
                            + "version int NOT NULL, "
                            + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                            + "PRIMARY KEY (id)) ENGINE=MyISAM;");
                }
				return null;
			}});
	}

	public void executeScript(final String script) throws DaoException {
		doInDao(new Lambda<Void>() {
			@Override
			public Void exec(Object... params) throws SQLException {
				String[] split = script.split("[;]");
				for (String sql : split) {
					if (sql.length() > 2 && !sql.startsWith("--")) {
						execute(sql);
					}
				}
				return null;
			}});
	}
	
	public int readLastSchemaVersion() throws DaoException {
		return doInDao(new Lambda<Integer>() {
			@Override
			public Integer exec(Object... params) throws SQLException {
				List<Integer> list = query(new RowTransformer<Integer>() {
					@Override
					public Integer transform(ResultSet resultSet) throws SQLException {
						return resultSet.getInt(Fields.MAXVERSION.name());
					}
				}, QueryBuilder.createQueryBuilder()
                        .select()
                        .max(Fields.VERSION)
                        .from(TABLE)
                        .build());
				if (list.size() > 0) {
					return list.get(0);
				}
				return 0;
			}});
	}

	public void setLastSchemaVersion(final int lastSchemaVersion) throws DaoException {
		doInDao(new Lambda<Void>() {
			@Override
			public Void exec(Object... params) throws SQLException {
				update("insert into schemaversion (version) values(?);", lastSchemaVersion);
				return null;
			}});
	}

}
