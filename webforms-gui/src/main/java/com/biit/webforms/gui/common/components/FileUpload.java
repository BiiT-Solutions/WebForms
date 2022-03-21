package com.biit.webforms.gui.common.components;

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