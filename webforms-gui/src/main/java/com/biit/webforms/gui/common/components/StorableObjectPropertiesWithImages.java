package com.biit.webforms.gui.common.components;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.gui.webpages.designer.ImagePreview;
import com.biit.webforms.gui.webpages.designer.ValidatorInteger;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.ElementWithImage;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;

public abstract class StorableObjectPropertiesWithImages<T extends StorableObject & ElementWithImage> extends StorableObjectProperties<T> {
	private static final long serialVersionUID = 894265579530046205L;
	private static final List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/gif");
	private static final int MAX_DISPLAY_FILE_NAME_LENGTH = 12;
	private static final String STATUS_STYLE = "status_field";
	private TextField imageFile, imageWidth, imageHeight;
	private ImagePreview imagePreview;
	private Button deleteImageButton;
	// Image uploader
	private Upload upload;
	// Put upload in this memory buffer that grows automatically
	private ByteArrayOutputStream imageMemoryOutputStream;

	private Label status;

	protected StorableObjectPropertiesWithImages(Class<? extends T> type) {
		super(type);
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	@Override
	protected void initElement() {
		super.initElement();
		createImageProperties();
		imageMemoryOutputStream = new ByteArrayOutputStream(10240);
	}

	@Override
	protected void initValues() {
		super.initValues();
		initImageValues();
	}

	private void initImageValues() {
		TreeObjectImage image = getInstance().getImage();
		if (image != null) {
			imageWidth.setValue(image.getWidth() + "");
			imageHeight.setValue(image.getHeight() + "");
			imageFile.setValue(image.getFileName());

			imageMemoryOutputStream = image.getStream();
			updatePreviewImagePanel();

			// Enable image area
			imagePreview.setVisible(true);
			deleteImageButton.setVisible(true);
		}
	}

	private void createImageProperties() {
		status = new Label(LanguageCodes.FILE_UPLOAD_CAPTION.translation());
		ImageReceiver receiver = new ImageReceiver();

		imageFile = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_FILE));
		imageFile.setEnabled(false);
		imageWidth = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_WIDTH));
		imageWidth.addValidator(new ValidatorInteger());
		imageHeight = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_HEIGHT));
		imageWidth.addValidator(new ValidatorInteger());

		FormLayout imageProperties = new FormLayout();
		imageProperties.setWidth(null);
		imageProperties.setHeight(null);
		imageProperties.addComponent(createUploader(LanguageCodes.FILE_UPLOAD_BUTTON_SELECT.translation(), receiver));
		imageProperties.addComponent(imageFile);
		imageProperties.addComponent(imageWidth);
		imageProperties.addComponent(imageHeight);

		imagePreview = new ImagePreview(1f);
		imagePreview.setWidth("200px");
		imagePreview.setVisible(false);
		// imagePreview.setHeight("200px");
		imageProperties.addComponent(imagePreview);
		updatePreviewImagePanel();

		// Delete button
		deleteImageButton = new Button(LanguageCodes.COMMON_CAPTION_DELETE.translation());
		imageProperties.addComponent(deleteImageButton);
		deleteImageButton.setVisible(false);
		deleteImageButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -391325312676441110L;

			@Override
			public void buttonClick(ClickEvent event) {
				cancelImage();
				//updateImageValue();
				status.setValue(LanguageCodes.FILE_DELETED.translation());
			}
		});

		if (WebformsConfigurationReader.getInstance().isImagesEnabled()) {
			addTab(imageProperties, ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_TITLE), false);
		}
	}

	private void cancelImage() {
		imagePreview.setStreamSource(null);
		imagePreview.setVisible(false);
		deleteImageButton.setVisible(false);
		// Disable field to disable events to be launched.
		imageWidth.setEnabled(false);
		imageHeight.setEnabled(false);
		imageFile.setValue("");
		imageHeight.setValue("");
		imageWidth.setValue("");
		// Disable field to disable events to be launched.
		imageWidth.setEnabled(true);
		imageHeight.setEnabled(true);
		imageMemoryOutputStream = null;
		getInstance().setImage(null);
	}

	private VerticalLayout createUploader(String currentUploadButtonText, ImageReceiver receiver) {
		final ProgressBar progressIndicator = new ProgressBar();
		final HorizontalLayout progressLayout = new HorizontalLayout();

		upload = new Upload(null, receiver);
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		status.addStyleName(STATUS_STYLE);

		layout.addComponent(status);
		layout.addComponent(upload);
		layout.addComponent(progressLayout);

		// Make uploading start immediately when file is selected
		upload.setImmediate(true);
		upload.setButtonCaption(currentUploadButtonText);

		progressLayout.setSpacing(true);
		progressLayout.setVisible(false);
		progressLayout.addComponent(progressIndicator);
		progressLayout.setComponentAlignment(progressIndicator, Alignment.MIDDLE_LEFT);

		final Button cancelProcessing = new Button(LanguageCodes.FILE_UPLOAD_BUTTON_CANCEL.translation());
		cancelProcessing.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -391325312676441110L;

			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
				imagePreview.setStreamSource(null);
			}
		});
		// cancelProcessing.setStyleName("small");
		progressLayout.addComponent(cancelProcessing);

		/**
		 * Add needed listener for the upload component: start, progress,
		 * finish, success, fail
		 */
		upload.addStartedListener(new Upload.StartedListener() {
			private static final long serialVersionUID = 7986347617060943929L;

			public void uploadStarted(StartedEvent event) {
				// Check correct files.
				String contentType = event.getMIMEType();
				boolean allowed = false;
				for (int i = 0; i < allowedMimeTypes.size(); i++) {
					if (contentType.equalsIgnoreCase(allowedMimeTypes.get(i))) {
						allowed = true;
						break;
					}
				}

				if (allowed) {
					// This method gets called immediately after upload is
					// started
					upload.setVisible(false);
					progressLayout.setVisible(true);
					progressIndicator.setValue(0f);
					status.setValue(ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_UPLOADING, new Object[] { event.getFilename() }));
				} else {
					MessageManager.showError(LanguageCodes.FILE_UPLOAD_INVALID.translation(), LanguageCodes.FILE_UPLOAD_INVALID_DESCRIPTION_TYPE.translation()
							+ " " + allowedMimeTypes);
					upload.interruptUpload();
					cancelImage();
				}

				// Check correct length
				if (event.getContentLength() > TreeObjectImage.MAX_IMAGE_LENGTH) {
					MessageManager.showError(ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_INVALID),
							ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_INVALID_DESCRIPTION_SIZE, new Object[] { TreeObjectImage.MAX_IMAGE_LENGTH }));
					upload.interruptUpload();
					cancelImage();
				}
			}
		});

		upload.addProgressListener(new Upload.ProgressListener() {
			private static final long serialVersionUID = -1930623560474608086L;

			public void updateProgress(long readBytes, long contentLength) {
				// This method gets called several times during the update
				progressIndicator.setValue(new Float(readBytes / (float) contentLength));
			}

		});

		upload.addSucceededListener(new Upload.SucceededListener() {
			private static final long serialVersionUID = 3256522293823783914L;

			public void uploadSucceeded(SucceededEvent event) {
				// This method gets called when the upload finished successfully
				status.setValue(ServerTranslate.translate(
						LanguageCodes.FILE_UPLOAD_SUCCESS,
						new Object[] { event.getFilename().length() < MAX_DISPLAY_FILE_NAME_LENGTH ? event.getFilename() : event.getFilename().substring(0,
								MAX_DISPLAY_FILE_NAME_LENGTH) }));
				upload.setButtonCaption(LanguageCodes.FILE_UPLOAD_BUTTON_UPDATE.translation());
				imageFile.setValue(event.getFilename());
				imagePreview.setVisible(true);
				deleteImageButton.setVisible(true);
				updatePreviewImagePanel();
				updateImageValue();
			}
		});

		upload.addFailedListener(new Upload.FailedListener() {
			private static final long serialVersionUID = -4554278465897865471L;

			public void uploadFailed(FailedEvent event) {
				// This method gets called when the upload failed
				status.setValue(LanguageCodes.FILE_UPLOAD_ERROR.translation());
				imagePreview.setVisible(false);
				deleteImageButton.setVisible(false);
			}
		});

		upload.addFinishedListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 4101238722584584073L;

			public void uploadFinished(FinishedEvent event) {
				// This method gets called always when the upload finished,
				// either succeeding or failing
				progressLayout.setVisible(false);
				upload.setVisible(true);
				// upload.setCaption("");
			}
		});

		return layout;
	}

	public class ImageReceiver implements Receiver {
		private static final long serialVersionUID = 6939666378311756261L;
		private String fileName;
		private String mtype;

		public OutputStream receiveUpload(String filename, String mimetype) {
			fileName = filename;
			mtype = mimetype;
			imageMemoryOutputStream.reset(); // If re-uploading
			return imageMemoryOutputStream;
		}

		public String getFileName() {
			return fileName;
		}

		public String getMimeType() {
			return mtype;
		}
	}

	private void updatePreviewImagePanel() {
		if (imageMemoryOutputStream != null) {
			// Display the image in the feedback component
			StreamSource source = new StreamSource() {
				private static final long serialVersionUID = -4905654404647215809L;

				public InputStream getStream() {
					return new ByteArrayInputStream(imageMemoryOutputStream.toByteArray());
				}
			};
			imagePreview.setStreamSource(source);
			BufferedImage bimg;
			try {
				bimg = ImageIO.read(source.getStream());
				if (bimg != null) {
					// Disable field to disable events to be launched.
					imageWidth.setEnabled(false);
					imageHeight.setEnabled(false);
					imageWidth.setValue(bimg.getWidth() + "");
					imageHeight.setValue(bimg.getHeight() + "");
					imageWidth.setEnabled(true);
					imageHeight.setEnabled(true);
				}
			} catch (IOException e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		} else {
			imagePreview.setStreamSource(null);
		}
	}

	@Override
	public void updateElement() {
		getInstance().setUpdatedBy(UserSessionHandler.getUser());
		getInstance().setUpdateTime();
		firePropertyUpdateListener(getInstance());
	}

	/**
	 * Sets the image to the instance.
	 */
	private void updateImageValue() {
		TreeObjectImage image = new TreeObjectImage();
		image.setCreatedBy(UserSessionHandler.getUser().getId());
		try {
			image.setWidth(Integer.parseInt(imageWidth.getValue()));
		} catch (NumberFormatException e) {
		}
		try {
			image.setHeight(Integer.parseInt(imageHeight.getValue()));
		} catch (NumberFormatException e) {
		}
		image.setFileName(imageFile.getValue());
		// imageMemoryOutputStream.reset();
		image.setStream(imageMemoryOutputStream);
		getInstance().setImage(image);
	}

}
