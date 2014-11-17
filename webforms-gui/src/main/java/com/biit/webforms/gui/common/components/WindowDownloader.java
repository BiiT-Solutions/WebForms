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
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * This is a window for generate/download new elements it has a default generate
 * view until the component is generated. The generation view can have a
 * progress bar with determined values or undetermined.
 * 
 */
public class WindowDownloader extends Window {
	private static final long serialVersionUID = -4779913287589899589L;
	private static final String WIDTH = "300px";
	private static final String HEIGHT = "150px";
	private static final String DEFAULT_FILENAME = "filename";

	private FileDownloader downloader;
	private StreamResource streamResource;
	private Button downloadButton;
	private InputStream dataStreamSource;
	private boolean resourceGenerated;
	private ProgressBar progressBar;
	private final WindowDownloaderProcess process;
	private Label messageLabel;

	public WindowDownloader(WindowDownloaderProcess process) {
		super();
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
		progressBar = new ProgressBar();
		configure();
		process();
	}

	private void configure() {
		setModal(true);
		setClosable(true);
		setResizable(false);
		setWidth(WIDTH);
		setHeight(HEIGHT);
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

	public void showCentered() {
		center();
		UI.getCurrent().addWindow(this);
	}
	
	public void setMessage(String value){
		this.messageLabel.setValue(value);
	}

	/**
	 * Makes visualization of progress bar as a undetermined clock.
	 * 
	 * @param value
	 */
	public void setIndeterminate(boolean value) {
		progressBar.setIndeterminate(value);
	}

	public void setProgress(float value) {
		progressBar.setValue(value);
	}

	protected void process() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();
		
		messageLabel = new Label();
		messageLabel.setWidth(null);
		rootLayout.addComponent(messageLabel);
		rootLayout.setComponentAlignment(messageLabel, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(messageLabel, 0.20f);
		
		rootLayout.addComponent(progressBar);
		rootLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(progressBar, 0.80f);

		setContent(rootLayout);
	}

	protected void showDownload() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		downloadButton = new Button();
		downloadButton.setCaption(LanguageCodes.CAPTION_DOWNLOAD_FILE.translation());
		downloadButton.setDescription(LanguageCodes.TOOLTIP_DOWNLOAD_FILE.translation());
		downloadButton.setIcon(CommonThemeIcon.FILE_DOWNLOAD.getThemeResource());

		downloader.extend(downloadButton);

		rootLayout.addComponent(downloadButton);
		rootLayout.setComponentAlignment(downloadButton, Alignment.MIDDLE_CENTER);

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
	 * Method to update Ui progress while working
	 */
	protected synchronized void updateProgress(final float value) {
		// Update the UI thread-safely
		UI.getCurrent().access(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(value);
			}
		});
	}

	/**
	 * This work manages all the inner work of the component to work off Ui and
	 * update when finished
	 * 
	 */
	class WorkThread extends Thread {
		@Override
		public void run() {
			setDataStreamSource(process.getInputStream());
			enableDownload();
		}
	};
}
