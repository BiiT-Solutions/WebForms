package com.biit.webforms.gui.webpages.formmanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.utils.GraphvizApp;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowImpactAnalysis extends WindowAcceptCancel {
	private static final long serialVersionUID = -7240113029111810959L;
	private static final String IMPACT_ANALYSIS_WIDTH = "350px";
	private static final String IMPACT_ANALYSIS_HEIGHT = "250px";
	private static final String LABEL_HEIGHT = "20px";
	private static final String VERSION_COMBOBOX_WIDTH = "100px";

	private Label formName;
	private ComboBox version;
	private IWebformsFormView form;

	public WindowImpactAnalysis() {
		init();
		setContent(generate());

		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				generateImpactAnalysis();
			}
		});
	}

	protected void generateImpactAnalysis() {
		WindowDownloader downloader = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					byte[] removedElementsFile = GraphvizApp.generateImageImpactAnalysisRemovedElements(
							loadForm(getSelectedVersion()), loadForm(form), ImgType.PDF);
					byte[] addedAndUpdatedElementsFile = GraphvizApp.generateImageImpactAnalysisAddedElements(
							loadForm(getSelectedVersion()), loadForm(form), ImgType.PDF);

					byte[] zipFile = zipFiles(removedElementsFile, addedAndUpdatedElementsFile);
					
					return new ByteArrayInputStream(zipFile);
				} catch (IOException | InterruptedException e) {
					WebformsLogger.errorMessage(this.getClass().getName(), e);
					return null;
				}
			}
		});
		downloader.setIndeterminate(true);
		downloader.setFilename(getFilename());
		downloader.showCentered();
	}

	private byte[] zipFiles(byte[] removedElementsFile, byte[] addedAndUpdatedElementsFile) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		addFileToZip(zos, removedElementsFile, getRemovedElementsFileName());
		addFileToZip(zos, addedAndUpdatedElementsFile, getAddedUpdatedElementsFileName());

		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

	private void addFileToZip(ZipOutputStream zos, byte[] data, String name) throws IOException {
		ZipEntry entry = new ZipEntry(name);
		entry.setSize(data.length);
		zos.putNextEntry(entry);
		zos.write(data);
	}

	private String getFilename() {
		return form.getLabel() + "_analysis_" + form.getVersion() + "_" + getSelectedVersion() + ".zip";
	}

	private String getRemovedElementsFileName() {
		return form.getLabel() + "_analysis_" + form.getVersion() + "_" + getSelectedVersion() + "_removed_elements"
				+ ".pdf";
	}

	private String getAddedUpdatedElementsFileName() {
		return form.getLabel() + "_analysis_" + form.getVersion() + "_" + getSelectedVersion() + "_changes" + ".pdf";
	}

	private SimpleFormView getSelectedVersion() {
		return (SimpleFormView) version.getValue();
	}

	private void init() {
		setWidth(IMPACT_ANALYSIS_WIDTH);
		setHeight(IMPACT_ANALYSIS_HEIGHT);
		setResizable(false);
		setModal(true);
	}

	private Component generate() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);

		formName = new Label();
		formName.setImmediate(true);
		formName.setWidth(null);
		formName.setHeight(LABEL_HEIGHT);

		version = new ComboBox(LanguageCodes.CAPTION_VERSION.translation());
		version.setWidth(VERSION_COMBOBOX_WIDTH);
		version.setImmediate(true);
		version.setNullSelectionAllowed(false);
		version.setTextInputAllowed(false);

		rootLayout.addComponent(formName);
		rootLayout.setComponentAlignment(formName, Alignment.MIDDLE_CENTER);
		rootLayout.addComponent(version);
		rootLayout.setComponentAlignment(version, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(version, 1.0f);

		return rootLayout;
	}

	public void setForm(IWebformsFormView form) {
		this.form = form;
		updateUi();
	}

	private void updateUi() {
		formName.setValue(form.getLabel());

		List<SimpleFormView> forms = UserSessionHandler.getController().getSimpleFormVersionsWebforms(form.getLabel(),
				form.getOrganizationId());

		for (SimpleFormView otherVersion : forms) {
			if (otherVersion.getVersion() < form.getVersion()) {
				version.addItem(otherVersion);
				version.setItemCaption(otherVersion, otherVersion.getVersion().toString());
			}
		}
		if (version.getItemIds().isEmpty()) {
			getAcceptButton().setEnabled(false);
			version.setEnabled(false);
		} else {
			version.setValue(version.getItemIds().iterator().next());
		}
	}

	private Form loadForm(IWebformsFormView formView) {
		Form form = UserSessionHandler.getController().loadForm(formView);
		form.setLastVersion(formView.isLastVersion());
		return form;
	}
}
