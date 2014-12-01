package com.biit.webforms.gui.common.components;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.language.LanguageCodes;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.WrapperTransferable;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class WindowUpload extends WindowAcceptCancel {
	private static final long serialVersionUID = -4455308150978606862L;
	private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB
	private static final String WINDOW_WIDTH = "640px";
	private static final String WINDOW_HEIGHT = "480px";
	private static final String CLASSNAME_WINDOW_UPLOAD = "window-upload-root-layout";

	private VerticalLayout dropLayout;
	private VerticalLayout uploadLayout;
	
	private Upload uploader;

	public WindowUpload() {
		super();
		configure();
		setContent(generate());
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setResizable(false);
		setClosable(false);
	}

	private Component generate() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		dropLayout = new VerticalLayout();
		dropLayout.setSizeFull();
		dropLayout.setStyleName(CLASSNAME_WINDOW_UPLOAD);

		UploadReceiver receiver = new UploadReceiver();
		uploader = new Upload("", receiver);
		uploader.addStartedListener(receiver);
		uploader.addFailedListener(receiver);
		uploader.addProgressListener(receiver);
		uploader.addSucceededListener(receiver);
		uploader.setWidth(null);

		setPutFilesLabel();

		uploadLayout = new VerticalLayout();
		uploadLayout.setWidth("100%");
		uploadLayout.setHeight(null);

		DragAndDropWrapper dragAndDropTarget = new DragAndDropWrapper(
				dropLayout);
		dragAndDropTarget.setDropHandler(new WindowUploadDropHandler());
		dragAndDropTarget.setSizeFull();

		rootLayout.addComponent(uploader);
		rootLayout.addComponent(dragAndDropTarget);
		rootLayout.setComponentAlignment(uploader, Alignment.TOP_CENTER);
		rootLayout.setExpandRatio(dragAndDropTarget, 1.0f);

		return rootLayout;
	}

	private class UploadReceiver implements Receiver, StartedListener,
			SucceededListener, ProgressListener, FailedListener {
		private static final long serialVersionUID = 4312735629599512861L;

		private FileUpload upload;
		private ByteArrayOutputStream bas;

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {

			if (upload != null && (!upload.isComplete())) {
				deleteFile(upload);
			}

			bas = new ByteArrayOutputStream();
			upload = new FileUpload();
			upload.setLabel(filename);
			upload.addDeleteClickListener(new ClickListener() {
				private static final long serialVersionUID = 8289911820038205380L;

				@Override
				public void buttonClick(ClickEvent event) {
					deleteFile(upload);
				}
			});
			addFile(upload);

			return bas;
		}

		@Override
		public void uploadStarted(StartedEvent event) {
			if(event.getContentLength() > FILE_SIZE_LIMIT){
				uploader.interruptUpload();
				MessageManager
				.showError(LanguageCodes.ERROR_FILE_TOO_LARGE);
			}
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			if (upload != null) {
				upload.setProgress((float) ((double) readBytes / (double) contentLength));
			}
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			if (upload != null) {
				upload.setStream(bas);
				check(upload);
			}
		}

		@Override
		public void uploadFailed(FailedEvent event) {
			if (upload != null) {
				deleteFile(upload);
			}
		}

	}

	private class WindowUploadDropHandler implements DropHandler {
		private static final long serialVersionUID = 440797950735022220L;

		@Override
		public void drop(DragAndDropEvent dropEvent) {
			// expecting this to be an html5 drag
			final WrapperTransferable tr = (WrapperTransferable) dropEvent
					.getTransferable();
			final Html5File[] files = tr.getFiles();
			if (files != null) {
				getAcceptButton().setEnabled(false);
				for (final Html5File html5File : files) {
					if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
						MessageManager
								.showError(LanguageCodes.ERROR_FILE_TOO_LARGE);
					} else {
						final ByteArrayOutputStream bas = new ByteArrayOutputStream();
						final StreamVariable streamVariable = new StreamVariable() {
							private static final long serialVersionUID = -3019269755448571834L;

							private FileUpload fileUpload = new FileUpload();

							@Override
							public OutputStream getOutputStream() {
								return bas;
							}

							@Override
							public boolean listenProgress() {
								return true;
							}

							@Override
							public void onProgress(
									final StreamingProgressEvent event) {
								fileUpload.setProgress((float) ((double) event
										.getBytesReceived() / (double) event
										.getContentLength()));
							}

							@Override
							public void streamingStarted(
									final StreamingStartEvent event) {
								fileUpload
										.addDeleteClickListener(new ClickListener() {
											private static final long serialVersionUID = 3508495853472647778L;

											@Override
											public void buttonClick(
													ClickEvent event) {
												deleteFile(fileUpload);
											}
										});
								fileUpload.setLabel(html5File.getFileName());
								addFile(fileUpload);
							}

							@Override
							public void streamingFinished(
									final StreamingEndEvent event) {
								fileUpload.setStream(bas);
								check(fileUpload);
							}

							@Override
							public void streamingFailed(
									final StreamingErrorEvent event) {
								deleteFile(fileUpload);
							}

							@Override
							public boolean isInterrupted() {
								return false;
							}
						};
						html5File.setStreamVariable(streamVariable);
					}
				}
				getAcceptButton().setEnabled(true);
			}
		}

		@Override
		public AcceptCriterion getAcceptCriterion() {
			return AcceptAll.get();
		}

	}

	/**
	 * Override to implement a check when the file has finished
	 * 
	 * @param fileUpload
	 */
	protected void check(FileUpload fileUpload) {

	}

	protected void addFile(FileUpload fileUpload) {
		if (!uploadLayout.isAttached()) {
			dropLayout.removeAllComponents();
			dropLayout.addComponent(uploadLayout);
		}
		uploadLayout.addComponent(fileUpload);
	}

	protected void deleteFile(FileUpload fileUpload) {
		uploadLayout.removeComponent(fileUpload);
		if (uploadLayout.getComponentCount() == 0) {
			dropLayout.removeAllComponents();
			setPutFilesLabel();
		}
	}

	private void setPutFilesLabel() {
		Label label = new Label(LanguageCodes.CAPTION_DRAG_AND_DROP_FILES.translation());
		label.setSizeUndefined();
		dropLayout.addComponent(label);
		dropLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
	}

	public List<UploadedFile> getFiles() {
		List<UploadedFile> uploadedFiles = new ArrayList<>();
		Iterator<Component> itr = uploadLayout.iterator();
		while (itr.hasNext()) {
			Component component = itr.next();
			if (component instanceof FileUpload) {
				UploadedFile file = ((FileUpload) component).getUploadedFile();
				if (file != null) {
					uploadedFiles.add(file);
				}
			} else {
				break;
			}
		}
		return uploadedFiles;
	}

	protected boolean acceptAction() {
			Iterator<Component> uploadItr = uploadLayout.iterator();
			while(uploadItr.hasNext()){
				Component component = uploadItr.next();
				if(component instanceof FileUpload){
					if(!((FileUpload) component).isComplete()){
						MessageManager.showError(LanguageCodes.ERROR_MESSAGE_FILES_UPLOAD_NOT_COMPLETED);
						return false;
					}
				} 
			}
			return true;
	}
}
