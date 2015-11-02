package com.biit.webforms.gui.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.PropertiesForClassComponent;
import com.biit.webforms.gui.common.language.CommonComponentsLanguageCodes;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.webpages.designer.ImagePreview;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.IWebformsSecurityService;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
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

public abstract class StorableObjectProperties<T extends StorableObject> extends PropertiesForClassComponent<T> {
	private static final long serialVersionUID = -1986275953105055523L;
	private static final List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/gif");
	private static final int MAX_DISPLAY_FILE_NAME_LENGTH = 12;
	private TextField createdByField, creationTimeField, updatedByField, updateTimeField;
	private TextField imageFile, imageWidth, imageHeight;
	private ImagePreview imagePreview;
	// Image uploader
	private ProgressBar progressIndicator = new ProgressBar();
	private ImageReceiver receiver = new ImageReceiver();
	private HorizontalLayout progressLayout = new HorizontalLayout();
	private Upload upload;
	// Put upload in this memory buffer that grows automatically
	final ByteArrayOutputStream imageMemoryOutputStream = new ByteArrayOutputStream(10240);

	private T instance;

	private IWebformsSecurityService webformsSecurityService;
	final Embedded image = new Embedded("Uploaded Image");

	protected StorableObjectProperties(Class<? extends T> type) {
		super(type);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		webformsSecurityService = (IWebformsSecurityService) helper.getBean("webformsSecurityService");
		initElement();
	}

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initElement() {
		createCommonProperties();
		createImageProperties();
	}

	private void createCommonProperties() {
		createdByField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATED_BY));
		createdByField.setEnabled(false);
		creationTimeField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_CREATION_TIME));
		creationTimeField.setEnabled(false);
		updatedByField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATED_BY));
		updatedByField.setEnabled(false);
		updateTimeField = new TextField(ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_UPDATE_TIME));
		updateTimeField.setEnabled(false);

		FormLayout commonProperties = new FormLayout();
		commonProperties.setWidth(null);
		commonProperties.setHeight(null);
		commonProperties.addComponent(createdByField);
		commonProperties.addComponent(creationTimeField);
		commonProperties.addComponent(updatedByField);
		commonProperties.addComponent(updateTimeField);

		addTab(commonProperties, ServerTranslate.translate(CommonComponentsLanguageCodes.TREE_OBJECT_PROPERTIES_COMMON_FORM_CAPTION), false);
	}

	private void createImageProperties() {
		imageFile = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_FILE));
		imageFile.setEnabled(false);
		imageWidth = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_WIDTH));
		imageHeight = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERITES_IMAGE_HEIGHT));

		FormLayout imageProperties = new FormLayout();
		imageProperties.setWidth(null);
		imageProperties.setHeight(null);
		imageProperties.addComponent(createUploader(LanguageCodes.FILE_UPLOAD_BUTTON_SELECT.translation()));
		imageProperties.addComponent(imageFile);
		imageProperties.addComponent(imageWidth);
		imageProperties.addComponent(imageHeight);

		imagePreview = new ImagePreview(1f);
		imagePreview.setWidth("200px");
		// imagePreview.setHeight("200px");
		imageProperties.addComponent(imagePreview);

		if (WebformsConfigurationReader.getInstance().isImagesEnabled()) {
			addTab(imageProperties, ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_TITLE), false);
		}
	}

	private VerticalLayout createUploader(String currentUploadButtonText) {
		upload = new Upload(null, receiver);
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);

		final Label status = new Label(LanguageCodes.FILE_UPLOAD_CAPTION.translation());

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
					MessageManager.showError(LanguageCodes.FILE_UPLOAD_INVALID.translation(), LanguageCodes.FILE_UPLOAD_INVALID_DESCRIPTION.translation() + " "
							+ allowedMimeTypes);
					upload.interruptUpload();
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
				updatePreviewImagePanel();
			}
		});

		upload.addFailedListener(new Upload.FailedListener() {
			private static final long serialVersionUID = -4554278465897865471L;

			public void uploadFailed(FailedEvent event) {
				// This method gets called when the upload failed
				status.setValue(LanguageCodes.FILE_UPLOAD_ERROR.translation());
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

	/**
	 * This method needs to be overwritten
	 * 
	 * @param element
	 */
	protected void initValues() {
		initCommonValues();
		initImageValues();
	}

	private void initCommonValues() {
		String valueCreatedBy = "";
		String valueUpdatedBy = "";

		if (getInstance() == null) {
			return;
		}

		try {
			valueCreatedBy = getInstance().getCreatedBy() == null ? "" : webformsSecurityService.getUserById(getInstance().getCreatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueCreatedBy = getInstance().getCreatedBy() + "";
		}

		try {
			valueUpdatedBy = getInstance().getUpdatedBy() == null ? "" : webformsSecurityService.getUserById(getInstance().getUpdatedBy()).getEmailAddress();
		} catch (UserDoesNotExistException udne) {
			valueUpdatedBy = getInstance().getUpdatedBy() + "";
		}

		String valueCreationTime = getInstance().getCreationTime() == null ? "" : getInstance().getCreationTime().toString();
		String valueUpdatedTime = getInstance().getUpdateTime() == null ? "" : getInstance().getUpdateTime().toString();

		createdByField.setValue(valueCreatedBy);
		creationTimeField.setValue(valueCreationTime);
		updatedByField.setValue(valueUpdatedBy);
		updateTimeField.setValue(valueUpdatedTime);
	}

	private void initImageValues() {

	}

	@Override
	protected void setElementAbstract(T element) {
		setInstance(element);
		initValues();
	}

	@Override
	public void updateElement() {
		getInstance().setUpdatedBy(UserSessionHandler.getUser());
		getInstance().setUpdateTime();
		// // Update common ui fields.
		// initValues();

		firePropertyUpdateListener(getInstance());
	}

	protected T getInstance() {
		return instance;
	}

	protected void setInstance(T instance) {
		this.instance = instance;
	}

	public IWebformsSecurityService getWebformsSecurityService() {
		return webformsSecurityService;
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
		}
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

}
