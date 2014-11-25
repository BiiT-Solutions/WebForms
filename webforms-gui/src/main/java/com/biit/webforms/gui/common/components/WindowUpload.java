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
import com.vaadin.ui.VerticalLayout;

public class WindowUpload extends WindowAcceptCancel {
	private static final long serialVersionUID = -4455308150978606862L;
	private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB
	private static final String WINDOW_WIDTH = "640px";
	private static final String WINDOW_HEIGHT = "480px";
	private static final String CLASSNAME_WINDOW_UPLOAD = "window-upload-root-layout";

	private VerticalLayout rootLayout;
	private VerticalLayout uploadLayout;

	public WindowUpload() {
		super();
		configure();
		setContent(generate());
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setResizable(false);
	}

	private Component generate() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setStyleName(CLASSNAME_WINDOW_UPLOAD);
		setPutFilesLabel();

		uploadLayout = new VerticalLayout();
		uploadLayout.setWidth("100%");
		uploadLayout.setHeight(null);

		DragAndDropWrapper dragAndDropTarget = new DragAndDropWrapper(rootLayout);
		dragAndDropTarget.setDropHandler(new WindowUploadDropHandler());
		dragAndDropTarget.setSizeFull();

		return dragAndDropTarget;
	}

	private class WindowUploadDropHandler implements DropHandler {
		private static final long serialVersionUID = 440797950735022220L;

		@Override
		public void drop(DragAndDropEvent dropEvent) {
			// expecting this to be an html5 drag
			final WrapperTransferable tr = (WrapperTransferable) dropEvent.getTransferable();
			final Html5File[] files = tr.getFiles();
			if (files != null) {
				getAcceptButton().setEnabled(false);
				for (final Html5File html5File : files) {
					if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
						MessageManager.showError(LanguageCodes.ERROR_FILE_TOO_LARGE);
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
							public void onProgress(final StreamingProgressEvent event) {
								fileUpload.setProgress((float) ((double) event.getBytesReceived() / (double) event
										.getContentLength()));
							}

							@Override
							public void streamingStarted(final StreamingStartEvent event) {
								fileUpload.addDeleteClickListener(new ClickListener() {
									private static final long serialVersionUID = 3508495853472647778L;

									@Override
									public void buttonClick(ClickEvent event) {
										deleteFile(fileUpload);
									}
								});
								fileUpload.setLabel(html5File.getFileName());
								addFile(fileUpload);
							}

							@Override
							public void streamingFinished(final StreamingEndEvent event) {
								fileUpload.setStream(bas);
								check(fileUpload);
							}

							@Override
							public void streamingFailed(final StreamingErrorEvent event) {
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
	 * @param fileUpload
	 */
	protected void check(FileUpload fileUpload) {
		
	}

	protected void addFile(FileUpload fileUpload) {
		if (!uploadLayout.isAttached()) {
			rootLayout.removeAllComponents();
			rootLayout.addComponent(uploadLayout);
		}
		uploadLayout.addComponent(fileUpload);
	}

	protected void deleteFile(FileUpload fileUpload) {
		uploadLayout.removeComponent(fileUpload);
		if (uploadLayout.getComponentCount() == 0) {
			rootLayout.removeAllComponents();
			setPutFilesLabel();
		}
	}

	private void setPutFilesLabel() {
		Label label = new Label("Drag here files from your computer");
		label.setSizeUndefined();
		rootLayout.addComponent(label);
		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
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
}
