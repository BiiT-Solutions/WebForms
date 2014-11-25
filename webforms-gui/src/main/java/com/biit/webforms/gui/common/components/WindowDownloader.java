package com.biit.webforms.gui.common.components;

import java.io.InputStream;

import com.biit.webforms.gui.common.theme.CommonThemeIcon;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	private InputStream dataStreamSource;
	private boolean resourceGenerated;
	private final WindowDownloaderProcess process;

	public WindowDownloader(WindowDownloaderProcess process) {
		super(LanguageCodes.TITLE_DOWNLOAD_FILE.translation());
		this.process = process;
		streamResource = new StreamResource(new StreamSource() {
			private static final long serialVersionUID = 9114614540149356692L;

			@Override
			public InputStream getStream() {
				return dataStreamSource;
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

	public synchronized void setDataStreamSource(InputStream dataStreamSource) {
		this.dataStreamSource = dataStreamSource;
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
			setDataStreamSource(process.getInputStream());
			enableDownload();
		}
	};
}
