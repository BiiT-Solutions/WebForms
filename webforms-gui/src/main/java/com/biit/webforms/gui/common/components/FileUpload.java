package com.biit.webforms.gui.common.components;

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

import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.ui.*;

import java.io.ByteArrayOutputStream;

/**
 * Custom component to signal the upload of a file from user to server.
 *
 */
public class FileUpload extends CustomComponent {
	private static final long serialVersionUID = 9180211992856371960L;
	private static final String FULL = "100%";
	private static final String FILE_LAYOUT_HEIGHT = "36px";
	private static final String DELETE_BUTTON_WIDTH = "20px";
	private static final String CLASSNAME = "biit-file-upload";

	private HorizontalLayout rootFileLayout;
	private Label label;
	private ProgressBar progressBar;
	private Button delete;

	private ByteArrayOutputStream stream;

	public FileUpload() {
		super();
		setWidth(FULL);
		setHeight(FILE_LAYOUT_HEIGHT);
		setCompositionRoot(generate());
	}

	public void setProgress(float progress) {
		progressBar.setValue(progress);
	}

	public void setLabel(String fileName) {
		label.setValue(fileName);
	}

	public Component generate() {
		rootFileLayout = new HorizontalLayout();
		rootFileLayout.setWidth(FULL);
		rootFileLayout.setHeight(FILE_LAYOUT_HEIGHT);
		rootFileLayout.setImmediate(true);
		rootFileLayout.setSpacing(true);
		rootFileLayout.setMargin(true);
		rootFileLayout.setStyleName(CLASSNAME);

		label = new Label();
		label.setSizeUndefined();
		label.setImmediate(true);

		progressBar = new ProgressBar();
		progressBar.setImmediate(true);

		delete = new IconButton(CommonThemeIcon.REMOVE, IconSize.MEDIUM, LanguageCodes.COMMON_TOOLTIP_DELETE);
		delete.setWidth(DELETE_BUTTON_WIDTH);

		rootFileLayout.addComponent(label);
		rootFileLayout.addComponent(progressBar);
		rootFileLayout.addComponent(delete);

		rootFileLayout.setExpandRatio(label, 1.0f);

		rootFileLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		rootFileLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		rootFileLayout.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);

		return rootFileLayout;
	}

	public ByteArrayOutputStream getStream() {
		return stream;
	}

	public void setStream(ByteArrayOutputStream stream) {
		this.stream = stream;
		progressBar.setVisible(false);
	}

	public void addDeleteClickListener(Button.ClickListener listener) {
		delete.addClickListener(listener);
	}

	/**
	 * Returns the uploaded file or null if the file was not uploaded correctly
	 *
	 * @return
	 */
	public UploadedFile getUploadedFile() {
		if (label.getValue() == null || stream == null) {
			return null;
		}
		return new UploadedFile(label.getValue(), stream);
	}

	public boolean isComplete() {
		return stream!=null;
	}
}
