package org.mili.application.dao;

import org.mili.application.util.Lambda;



public class Dao extends JdbcTemplate {

	public <T> T doInDao(Lambda<T> lambda, Object... objects) throws DaoException {
		try {
			return lambda.exec();
		} catch(Exception e) {
			throw new DaoException(e);
		}
	}
}
