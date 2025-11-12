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

import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a window for generate/download new elements it has a default generate view until the component is generated.
 * The generation view can have a progress bar with determined values or undetermined.
 *
 */
public class WindowDownloader extends WindowProgressBar {
	private static final long serialVersionUID = -4779913287589899589L;
	private static final String DEFAULT_FILENAME = "filename";

	private FileDownloader downloader;
	private StreamResource streamResource;
	private Button downloadButton;
	private byte[] dataSource;
	private boolean resourceGenerated;
	private final WindowDownloaderProcess process;

	public WindowDownloader(WindowDownloaderProcess process) {
		super(LanguageCodes.TITLE_DOWNLOAD_FILE.translation());
		this.process = process;
		streamResource = new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 9114614540149356692L;

			@Override
			public InputStream getStream() {
				return new ByteArrayInputStream(dataSource);
			}
		}, DEFAULT_FILENAME);
		downloader = new FileDownloader(streamResource);
		resourceGenerated = false;
		configure();
		process(LanguageCodes.CAPTION_GENERATING_FILE.translation());
	}

	@Override
	public void configure() {
		super.configure();
		addAttachListener(new AttachListener() {
			private static final long serialVersionUID = -7628271453732026702L;

			@Override
			public void attach(AttachEvent event) {
				if (!resourceGenerated) {
					resourceGenerated = true;

					process.registerWindowDownloader((WindowDownloader) event.getSource());
					final WorkThread thread = new WorkThread();
					thread.start();
				}
			}
		});
	}

	public void setFilename(String filename) {
		streamResource.setFilename(filename);
	}

	protected void showDownload() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();

		Label messageLabel = new Label();
		messageLabel.setWidth(null);
		messageLabel.setValue(LanguageCodes.CAPTION_GENERATED_FILE.translation());
		rootLayout.addComponent(messageLabel);
		rootLayout.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
		rootLayout.setExpandRatio(messageLabel, 0.30f);

		downloadButton = new Button();
		downloadButton.setCaption(LanguageCodes.CAPTION_DOWNLOAD_FILE.translation());
		downloadButton.setDescription(LanguageCodes.TOOLTIP_DOWNLOAD_FILE.translation());
		downloadButton.setIcon(CommonThemeIcon.FILE_DOWNLOAD.getThemeResource());

		downloader.extend(downloadButton);

		rootLayout.addComponent(downloadButton);
		rootLayout.setComponentAlignment(downloadButton, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(downloadButton, 0.70f);

		setContent(rootLayout);
	}

	public synchronized void setDataSource(byte[] dataStreamSource) {
		this.dataSource = dataStreamSource;
	}

	/**
	 * Method to update Ui with download button
	 */
	protected synchronized void enableDownload() {
		// Update the UI thread-safely
		UI.getCurrent().access(new Runnable() {
			@Override
			public void run() {
				showDownload();
			}
		});
	}

	/**
	 * This work manages all the inner work of the component to work off Ui and update when finished
	 */
	class WorkThread extends Thread {
		@Override
		public void run() {
			try {
				InputStream stream = process.getInputStream();
				byte[] data = new byte[stream.available()];
				stream.read(data);
				setDataSource(data);
				enableDownload();
			} catch (IOException e) {
				WebformsUiLogger.errorMessage(this.getClass().getName(), e);
				MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
			}		    
		}
	};
}
