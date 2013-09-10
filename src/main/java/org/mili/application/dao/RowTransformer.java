package org.mili.application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowTransformer<T> {
	
	T transform(ResultSet resultSet) throws SQLException;

}
