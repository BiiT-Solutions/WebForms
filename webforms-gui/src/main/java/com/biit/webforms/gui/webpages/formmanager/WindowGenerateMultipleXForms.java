package com.biit.webforms.gui.webpages.formmanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.ZipTools;
import com.biit.webforms.xforms.XFormsMultiplesFormsExporter;
import com.biit.webforms.xforms.exceptions.InvalidDateException;
import com.biit.webforms.xforms.exceptions.NotExistingDynamicFieldException;
import com.biit.webforms.xforms.exceptions.PostCodeRuleSyntaxError;
import com.biit.webforms.xforms.exceptions.StringRuleSyntaxError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowGenerateMultipleXForms extends WindowAcceptCancel {
	private static final long serialVersionUID = -3167579242870259658L;
	private static final String WINDOW_WIDTH = "350px";
	private static final String WINDOW_HEIGHT = "220px";
	private Form form;

	public WindowGenerateMultipleXForms(Form form) {
		super();
		configure();
		setContent(generateContent());
		setValue(form);
	}

	private Component generateContent() {
		setCaption(LanguageCodes.TITLE_DOWNLOAD_FILE.translation());
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		Label label = new Label(LanguageCodes.CAPTION_GENERATING_FILE.translation());
		label.setWidth(null);

		rootLayout.addComponent(label);

		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	private void setValue(Form form) {
		this.form = form;
	}

	private void configure() {
		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
		setModal(true);
		setClosable(true);
		setResizable(false);
		setDraggable(false);
		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				exportXForms();
			}
		});
	}

	protected void exportXForms() {
		WindowDownloader downloader = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					List<String> xmlFiles = new ArrayList<>();
					List<String> xmlFileNames = new ArrayList<>();
					XFormsMultiplesFormsExporter formExporter = new XFormsMultiplesFormsExporter(form);
					xmlFiles.add(formExporter.getInitialInstancePage());
					xmlFileNames.add("initial-instance.xml");
					xmlFiles.add(formExporter.getCategoriesFlowPage());
					xmlFileNames.add("page-flow.xml");
					xmlFiles.addAll(formExporter.getAllcategoryModelPages());
					xmlFileNames.addAll(formExporter.getAllCategoriesFileNames());

					byte[] zipFile = ZipTools.zipFiles(xmlFiles, xmlFileNames);

					return new ByteArrayInputStream(zipFile);
				} catch (IOException | NotValidTreeObjectException | NotValidChildException
						| NotExistingDynamicFieldException | InvalidDateException | StringRuleSyntaxError
						| PostCodeRuleSyntaxError e) {
					MessageManager.showError(LanguageCodes.COMMON_ERROR_UNEXPECTED_ERROR);
					WebformsLogger.errorMessage(this.getClass().getName(), e);
					return null;
				}
			}
		});
		downloader.setIndeterminate(true);
		downloader.setFilename(getFilename());
		downloader.showCentered();
	}

	private String getFilename() {
		return form.getLabel() + ".zip";
	}

}
