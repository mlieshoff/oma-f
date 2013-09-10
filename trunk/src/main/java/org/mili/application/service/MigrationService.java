package org.mili.application.service;

import org.mili.application.service.Service;
import org.mili.application.service.ServiceException;
import org.mili.application.migration.Migrator;
import org.mili.application.util.Lambda;

public class MigrationService extends Service {

	public void migrate(final boolean dropAndCreate) throws ServiceException {
		doInService(new Lambda<Void>() {
			@Override
			public Void exec(Object... params) throws Exception {
				new Migrator().migrate(dropAndCreate);
				return null;
			}
		});
	}
	
}
