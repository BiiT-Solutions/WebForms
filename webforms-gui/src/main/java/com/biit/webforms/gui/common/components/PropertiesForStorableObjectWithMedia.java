package com.biit.webforms.gui.common.components;

import com.biit.persistence.entity.StorableObject;
import com.biit.utils.image.ImageTools;
import com.biit.utils.image.exceptions.InvalidRemoteImageDefinition;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.language.ServerTranslate;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.gui.webpages.designer.ImagePreview;
import com.biit.webforms.gui.webpages.designer.ValidatorInteger;
import com.biit.webforms.gui.webpages.designer.WindowSetUrl;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.ElementWithMedia;
import com.biit.webforms.persistence.entity.TreeObjectAudio;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.TreeObjectVideo;
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
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public abstract class PropertiesForStorableObjectWithMedia<T extends StorableObject & ElementWithMedia> extends
        StorableObjectProperties<T> {
    private static final long serialVersionUID = 894265579530046205L;
    private static final List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/gif");
    private static final int MAX_DISPLAY_FILE_NAME_LENGTH = 12;
    private static final String STATUS_STYLE = "status_field";
    private TextField imageFile, imageWidth, imageHeight;
    private ImagePreview imagePreview;
    private Button deleteImageButton, urlButton;
    // Image uploader
    private Upload upload;
    // Put upload in this memory buffer that grows automatically
    private ByteArrayOutputStream imageMemoryOutputStream;

    private TextField videoUrl, videoWidth, videoHeight;

    private TextField audioUrl;

    private Label status;

    protected PropertiesForStorableObjectWithMedia(Class<? extends T> type) {
        super(type);
    }

    /**
     * This method needs to be overwritten
     */
    @Override
    protected void initElement() {
        super.initElement();
        createImageProperties();
        createVideoProperties();
        createAudioProperties();
        imageMemoryOutputStream = new ByteArrayOutputStream(10240);
    }

    @Override
    protected void initValues() {
        super.initValues();
        initImageValues();
        initVideoValues();
        initAudioValues();
    }

    private void initImageValues() {
        TreeObjectImage image = getInstance().getImage();
        if (image != null) {
            imageWidth.setValue(image.getWidth() + "");
            imageHeight.setValue(image.getHeight() + "");
            if (image.getUrl() != null) {
                imageFile.setValue(image.getUrl());
            } else {
                imageFile.setValue(image.getFileName());
            }

            imageMemoryOutputStream = image.getStream();
            updatePreviewImagePanel();

            // Enable image area
            imagePreview.setVisible(true);
            deleteImageButton.setVisible(true);
        }
    }

    private void initVideoValues() {
        TreeObjectVideo video = getInstance().getVideo();
        if (video != null) {
            videoUrl.setValue(video.getUrl());
            videoWidth.setValue(video.getWidth() + "");
            videoHeight.setValue(video.getHeight() + "");
        }
    }

    private void initAudioValues() {
        TreeObjectAudio audio = getInstance().getAudio();
        if (audio != null) {
            audioUrl.setValue(audio.getUrl());
        }
    }

    private void createImageProperties() {
        status = new Label(LanguageCodes.FILE_UPLOAD_CAPTION.translation());
        ImageReceiver receiver = new ImageReceiver();

        imageFile = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_FILE));
        imageFile.setEnabled(false);
        imageWidth = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_WIDTH));
        imageWidth.addValidator(new ValidatorInteger());
        imageHeight = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_HEIGHT));
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
        imageProperties.addComponent(imagePreview);
        updatePreviewImagePanel();

        // Delete button
        deleteImageButton = new Button(LanguageCodes.COMMON_CAPTION_DELETE.translation());
        imageProperties.addComponent(deleteImageButton);
        deleteImageButton.setVisible(false);
        deleteImageButton.addClickListener((ClickListener) event -> {
            cancelImage();
            // updateImageValue();
            status.setValue(LanguageCodes.FILE_DELETED.translation());
            updateElement();
        });

        if (WebformsConfigurationReader.getInstance().isImagesEnabled()) {
            addTab(imageProperties, ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_TITLE), false);
        }
    }

    private void createVideoProperties() {
        videoUrl = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_VIDEO_URL));
        videoWidth = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_WIDTH));
        videoWidth.addValidator(new ValidatorInteger());
        videoHeight = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_IMAGE_HEIGHT));
        videoHeight.addValidator(new ValidatorInteger());
        FormLayout videoProperties = new FormLayout();
        videoProperties.setWidth(null);
        videoProperties.setHeight(null);
        videoProperties.addComponent(videoUrl);
        videoProperties.addComponent(videoWidth);
        videoProperties.addComponent(videoHeight);
        if (WebformsConfigurationReader.getInstance().isImagesEnabled()) {
            addTab(videoProperties, ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_VIDEO_TITLE), false);
        }
    }

    private void createAudioProperties() {
        audioUrl = new TextField(ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_AUDIO_URL));
        FormLayout audioProperties = new FormLayout();
        audioProperties.setWidth(null);
        audioProperties.setHeight(null);
        audioProperties.addComponent(audioUrl);
        if (WebformsConfigurationReader.getInstance().isImagesEnabled()) {
            addTab(audioProperties, ServerTranslate.translate(LanguageCodes.CAPTION_PROPERTIES_AUDIO_TITLE), false);
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
        // getInstance().setImage(null);
    }

    private void displayImage() {
        imagePreview.setVisible(true);
        deleteImageButton.setVisible(true);
        updatePreviewImagePanel();
        updateElement();
    }

    private VerticalLayout createUploader(String currentUploadButtonText, ImageReceiver receiver) {
        final ProgressBar progressIndicator = new ProgressBar();
        final HorizontalLayout progressLayout = new HorizontalLayout();

        upload = new Upload(null, receiver);
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        status.addStyleName(STATUS_STYLE);

        layout.addComponent(status);

        urlButton = new Button(LanguageCodes.CAPTION_PROPERTIES_IMAGE_URL.translation());

        urlButton.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 2404191435324077647L;

            @Override
            public void buttonClick(ClickEvent event) {
                WindowSetUrl window = new WindowSetUrl();
                window.addAcceptActionListener(new AcceptActionListener() {

                    @Override
                    public void acceptAction(WindowAcceptCancel window) {
                        imageFile.setValue(((WindowSetUrl) window).getUrl());

                        // Obtain image for preview.
                        byte[] byteArray;
                        try {
                            byteArray = ImageTools.getImageFromUrl(((WindowSetUrl) window).getUrl());
                            imageMemoryOutputStream = new ByteArrayOutputStream(byteArray.length);
                            imageMemoryOutputStream.write(byteArray, 0, byteArray.length);
                        } catch (InvalidRemoteImageDefinition e) {
                            imageMemoryOutputStream = null;
                            WebformsUiLogger.warning(this.getClass().getName(), e.getMessage());
                        }

                        displayImage();
                        window.close();
                    }
                });
                window.showCentered();
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponent(upload);
        buttonsLayout.addComponent(urlButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidth("100%");
        layout.addComponent(buttonsLayout);
        layout.addComponent(progressLayout);

        // Make uploading start immediately when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption(currentUploadButtonText);

        progressLayout.setSpacing(true);
        progressLayout.setVisible(false);
        progressLayout.addComponent(progressIndicator);
        progressLayout.setComponentAlignment(progressIndicator, Alignment.MIDDLE_LEFT);

        final Button cancelProcessing = new Button(LanguageCodes.FILE_UPLOAD_BUTTON_CANCEL.translation());
        cancelProcessing.addClickListener((ClickListener) event -> {
            upload.interruptUpload();
            imagePreview.setStreamSource(null);
        });
        // cancelProcessing.setStyleName("small");
        progressLayout.addComponent(cancelProcessing);

        /**
         * Add needed listener for the upload component: start, progress,
         * finish, success, fail
         */
        upload.addStartedListener((StartedListener) event -> {
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
                status.setValue(ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_UPLOADING, new Object[]{event.getFilename()}));
            } else {
                MessageManager.showError(LanguageCodes.FILE_UPLOAD_INVALID.translation(),
                        LanguageCodes.FILE_UPLOAD_INVALID_DESCRIPTION_TYPE.translation() + " " + allowedMimeTypes);
                upload.interruptUpload();
                cancelImage();
            }

            // Check correct length
            if (event.getContentLength() > TreeObjectImage.MAX_IMAGE_LENGTH) {
                MessageManager.showError(ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_INVALID), ServerTranslate.translate(
                        LanguageCodes.FILE_UPLOAD_INVALID_DESCRIPTION_SIZE, new Object[]{TreeObjectImage.MAX_IMAGE_LENGTH}));
                upload.interruptUpload();
                cancelImage();
            }
        });

        upload.addProgressListener((ProgressListener) (readBytes, contentLength) -> {
            // This method gets called several times during the update
            progressIndicator.setValue(readBytes / (float) contentLength);
        });

        upload.addSucceededListener((SucceededListener) event -> {
            // This method gets called when the upload finished successfully
            status.setValue(ServerTranslate.translate(LanguageCodes.FILE_UPLOAD_SUCCESS,
                    new Object[]{event.getFilename().length() < MAX_DISPLAY_FILE_NAME_LENGTH ? event.getFilename() : event
                            .getFilename().substring(0, MAX_DISPLAY_FILE_NAME_LENGTH)}));
            upload.setButtonCaption(LanguageCodes.FILE_UPLOAD_BUTTON_UPDATE.translation());
            displayImage();
            imageFile.setValue(event.getFilename());
        });

        upload.addFailedListener((FailedListener) event -> {
            // This method gets called when the upload failed
            status.setValue(LanguageCodes.FILE_UPLOAD_ERROR.translation());
            imagePreview.setVisible(false);
            deleteImageButton.setVisible(false);
        });

        upload.addFinishedListener((FinishedListener) event -> {
            // This method gets called always when the upload finished,
            // either succeeding or failing
            progressLayout.setVisible(false);
            upload.setVisible(true);
            // upload.setCaption("");
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
            if (imageMemoryOutputStream != null) {
                imageMemoryOutputStream.reset(); // If re-uploading
            }
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
            StreamSource source = () -> new ByteArrayInputStream(imageMemoryOutputStream.toByteArray());
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
                WebformsUiLogger.errorMessage(this.getClass().getName(), e);
            }
        } else {
            imagePreview.setStreamSource(null);
        }
    }

    @Override
    public void updateElement() {
        getInstance().setUpdatedBy(UserSession.getUser());
        getInstance().setUpdateTime();
        firePropertyUpdateListener(getInstance());
    }

    public TreeObjectImage getImage() {
        Integer width = null;
        Integer height = null;
        try {
            width = Integer.parseInt(imageWidth.getValue());
        } catch (NumberFormatException e) {
        }
        try {
            height = Integer.parseInt(imageHeight.getValue());
        } catch (NumberFormatException e) {
        }

        if (width == null || height == null || width.equals(0) || height.equals(0)) {
            return null;
        }
        TreeObjectImage image = new TreeObjectImage();
        image.setCreatedBy(UserSession.getUser().getUniqueId());
        try {
            image.setWidth(Integer.parseInt(imageWidth.getValue()));
        } catch (NumberFormatException e) {
        }
        try {
            image.setHeight(Integer.parseInt(imageHeight.getValue()));
        } catch (NumberFormatException e) {
        }

        if (imageFile.getValue() != null && imageFile.getValue().startsWith("http")) {
            image.setUrl(imageFile.getValue());
        } else {
            image.setFileName(imageFile.getValue());
            image.setStream(imageMemoryOutputStream);
        }

        return image;
    }


    public TreeObjectVideo getVideo() {
        if (videoUrl.getValue() == null || videoUrl.getValue().trim().isEmpty()) {
            return null;
        }
        final TreeObjectVideo video = new TreeObjectVideo();
        video.setUrl(videoUrl.getValue());

        video.setCreatedBy(UserSession.getUser().getUniqueId());
        try {
            video.setWidth(Integer.parseInt(videoWidth.getValue()));
        } catch (NumberFormatException e) {
        }
        try {
            video.setHeight(Integer.parseInt(videoHeight.getValue()));
        } catch (NumberFormatException e) {
        }

        return video;
    }

    public TreeObjectAudio getAudio() {
        if (audioUrl.getValue() == null || audioUrl.getValue().trim().isEmpty()) {
            return null;
        }
        final TreeObjectAudio audio = new TreeObjectAudio();
        audio.setUrl(audioUrl.getValue());
        return audio;
    }

}
