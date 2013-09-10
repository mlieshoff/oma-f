package org.mili.application.migration;

import org.apache.commons.io.FileUtils;
import org.mili.application.dao.DaoException;
import org.mili.application.dao.DaoFactory;
import org.mili.application.dao.SchemaVersionDao;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Migrator {

	public void migrate(boolean dropAndCreate) throws DaoException, IOException {
		SchemaVersionDao schemaVersionDao = DaoFactory.getDao(SchemaVersionDao.class);
		schemaVersionDao.init(dropAndCreate);
		boolean fileExists = true;
		while(fileExists) {
			int newSchemaVersion = schemaVersionDao.readLastSchemaVersion() + 1;
			System.out.println("search migration: " + String.format("scripts/migration_%s.sql", newSchemaVersion));
			// load script
			URL url = ClassLoader.getSystemResource(String.format("scripts/migration_%s.sql", newSchemaVersion));
			fileExists = url != null;
			if (fileExists) {
				// execute script
				System.out.println("execute migration: " + url.getFile());
				String script = FileUtils.readFileToString(new File(url.getFile()));
				schemaVersionDao.executeScript(script);
				// set new schema version
				schemaVersionDao.setLastSchemaVersion(newSchemaVersion);
			}
		}
	}

}
