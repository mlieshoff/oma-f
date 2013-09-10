package org.mili.application.service;

import org.mili.application.dao.ConnectionPool;
import org.mili.application.util.Lambda;
import org.mili.application.util.Log;

import java.sql.Connection;
import java.sql.SQLException;


public class Service {
    private ConnectionPool connectionPool = new ConnectionPool();

	public <T> T doInService(Lambda<T> lambda, Object... objects) throws ServiceException {
        ConnectionPool.ConnectionContext connectionContext = enable();
        connectionContext.setInTransaction(true);
        Connection connection = connectionContext.getConnection();
        try {
			return lambda.exec();
		} catch(Exception e) {
            rollback(connection);
			throw new ServiceException(e);
		} finally {
            commit(connection);
            disable(connection);
            connectionContext.setInTransaction(false);
        }
	}

    private ConnectionPool.ConnectionContext enable() throws ServiceException {
        try {
            ConnectionPool.ConnectionContext connectionContext = connectionPool.get();
            Connection connection = connectionContext.getConnection();
            if (!connectionContext.isInTransaction()) {
                connection.setAutoCommit(false);
            }
            Log.log(this, "enable", "enable transaction: %s", connection);
            return connectionContext;
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    private void disable(Connection connection) throws ServiceException {
        try {
            Log.log(this, "disable", "disable transaction: %s", connection);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    private void rollback(Connection connection) throws ServiceException {
        try {
            Log.log(this, "rollback", "rollback transaction: %s", connection);
            connection.rollback();
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    private void commit(Connection connection) throws ServiceException {
        try {
            Log.log(this, "commit", "commit transaction: %s", connection);
            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            throw new ServiceException(e);
        }
    }

    public <T> T doReadInService(Lambda<T> lambda, Object... objects) throws ServiceException {
        try {
            return lambda.exec();
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }

}
