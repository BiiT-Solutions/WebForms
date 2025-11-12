package com.biit.webforms.gui.webpages.formmanager;

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

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.CompleteFormView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WindowDownloaderJson extends WindowDownloader {
    private static final long serialVersionUID = 1327278107256149670L;

    public WindowDownloaderJson(final CompleteFormView completeFormView, String filename) {
        super(new WindowDownloaderProcess() {

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(completeFormView.toJson().getBytes(StandardCharsets.UTF_8));
            }
        });
        setIndeterminate(true);
        setFilename(filename);
        showCentered();
    }

    public WindowDownloaderJson(final com.biit.abcd.persistence.entity.Form form, String filename) {
        super(new WindowDownloaderProcess() {

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(form.toJson().getBytes(StandardCharsets.UTF_8));
            }
        });
        setIndeterminate(true);
        setFilename(filename);
        showCentered();
    }
}
