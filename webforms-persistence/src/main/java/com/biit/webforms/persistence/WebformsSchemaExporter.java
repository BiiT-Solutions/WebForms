package com.biit.webforms.persistence;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.persistence.HibernateDialect;
import com.biit.persistence.JpaSchemaExporter;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class WebformsSchemaExporter extends com.biit.persistence.JpaSchemaExporter {
	private static final String[] TABLES_TO_MODIFY = new String[] { "tree_forms", "tree_blocks", "tree_blocks_references" };

	public WebformsSchemaExporter(String[] packagesName, String[] classesToIgnore) {
		super(packagesName, classesToIgnore);
	}

	/**
	 * Create a script that can generate a database for the selected dialect
	 *
	 */
	@Override
	public void createDatabaseScript(HibernateDialect dialect, String directory, String outputFile, String host, String port, String username, String password,
			String databaseName, boolean onlyCreation) {
		super.createDatabaseScript(dialect, directory, outputFile, host, port, username, password, databaseName, onlyCreation);
		updateTables(directory, outputFile);
	}

	/**
	 * Changes some Hibernate wrong definitions.
	 */
	private void updateTables(String directory, String outputFile) {
		for (String tableName : TABLES_TO_MODIFY) {
			replaceFormLabel(tableName, directory, outputFile);
		}
	}

	private void replaceFormLabel(String tableName, String directory, String outputFile) {
		String oldFileName = directory + File.separator + outputFile;
		String tmpFileName = System.getProperty("java.io.tmpdir") + File.separator + outputFile;

		try {
			List<String> lines = Files.readAllLines(Paths.get(oldFileName), Charset.defaultCharset());
			boolean correctTable = false;

			// Replace values
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).contains("create table ")) {
					// Starting of a table.
					// Starting of the correct table.
					correctTable = lines.get(i).contains("create table " + tableName + " (");
				}
				if (correctTable && lines.get(i).contains("label varchar(1000)")) {
					lines.set(i, lines.get(i).replace("label varchar(1000)", "label varchar(190)"));
				}
				if (correctTable && lines.get(i).contains("formLabel varchar(1000)")) {
					lines.set(i, lines.get(i).replace("formLabel varchar(1000)", "formLabel varchar(190)"));
				}
			}

			// Store lines.
			Path out = Paths.get(tmpFileName);
			Files.write(out, lines, Charset.defaultCharset());
		} catch (Exception e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return;
		}
		File oldFile = new File(oldFileName);
		if (oldFile.delete()) {
			// Once everything is complete, replace old file..
			Path source = Paths.get(tmpFileName);
			Path destination = Paths.get(oldFileName);
			try {
				Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
	}

	public static void main(String[] args) {
		setArguments(args);

		// Launch the JpaSchemaExporter
		JpaSchemaExporter gen = new WebformsSchemaExporter(getPacketsToScan(), getClassesToIgnoreWhenCreatingDatabase());
		gen.createDatabaseScript(HibernateDialect.MYSQL, getDirectory(), getOutputFile(), getHost(), getPort(), getUser(), getPassword(), getDatabaseName(), true);
		gen = new WebformsSchemaExporter(getPacketsToScan(), getClassesToIgnoreWhenUpdatingDatabase());
		gen.updateDatabaseScript(HibernateDialect.MYSQL, getDirectory(), getHost(), getPort(), getUser(), getPassword(), getDatabaseName());

		// Add hibernate sequence table.
		//addTextToFile(createHibernateSequenceTable(), getDirectory() + File.separator + getOutputFile());
		// Add extra information from an external script.
		addTextToFile(readFile(getScriptsToAdd(), StandardCharsets.UTF_8), getDirectory() + File.separator + getOutputFile());

		//Close file watchers to ensure that the thread ends and maven exec is not frozen. 
		WebformsConfigurationReader.getInstance().stopFileWatchers();
	}
}
