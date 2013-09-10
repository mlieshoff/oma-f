package org.mili.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

	private final static ThreadLocal<ConnectionContext> cache = new ThreadLocal<>();

	public ConnectionContext get() throws SQLException {
		if (cache.get() == null) {
			cache.set(getConnection());
		}
		return cache.get();
	}

	private ConnectionContext getConnection() throws SQLException {
		try {
            Class.forName("org.gjt.mm.mysql.Driver");
            return new ConnectionContext(DriverManager.getConnection("jdbc:mysql://localhost:3306/ticketeer?useUnicode=true&characterEncoding=UTF-8", "root",
                    ""));
        } catch (Exception e) {
        	throw new SQLException(e);
        }	
	}

	public void close() {
		if (cache.get() != null) {
			cache.remove();
		}
	}

    public static class ConnectionContext {
        private boolean inTransaction;
        private Connection connection;
        private long creationTime = System.currentTimeMillis();

        public ConnectionContext(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public boolean isInTransaction() {
            return inTransaction;
        }

        public void setInTransaction(boolean inTransaction) {
            this.inTransaction = inTransaction;
        }
    }

}
