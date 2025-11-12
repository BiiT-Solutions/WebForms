package com.biit.webforms.gui.common.utils;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import java.io.ByteArrayOutputStream;

/**
 * Struct like class to pass the data of a file that has been uploaded to the
 * server.
 *
 */
public class UploadedFile {

	private final String fileName;
	private final ByteArrayOutputStream stream;

	public UploadedFile(String filename, ByteArrayOutputStream stream) {
		this.fileName = filename;
		this.stream = stream;
	}

	public String getFileName() {
		return fileName;
	}

	public ByteArrayOutputStream getStream() {
		return stream;
	}

}
