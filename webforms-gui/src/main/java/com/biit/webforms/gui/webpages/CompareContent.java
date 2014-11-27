package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.compare.content.TableResultComparation;
import com.biit.webforms.gui.webpages.compare.content.TableXmlFiles;
import com.biit.webforms.gui.webpages.compare.content.UpperMenu;
import com.biit.webforms.gui.webpages.compare.structure.XmlWindowUpload;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.xml.CompareXmlToXml;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;

public class CompareContent extends SecuredWebPage {
	private static final long serialVersionUID = 8670784419625264277L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));
	private static final String RESULT_WIDTH = "140px";

	private UpperMenu upperMenu;

	private TableXmlFiles originalFiles;
	private TableXmlFiles processedFiles;

	private TableResultComparation resultComparation;

	private TextArea comparationResult;

	private ValueChangeOriginalXml valueChangeOriginal;
	private ValueChangeProcessedXml valueChangeProcessed;
	private ValueChangeResultXml valueChangeResult;

	@Override
	protected void initContent() {
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);

		FormEditBottomMenu bottomMenu = new FormEditBottomMenu();
		bottomMenu.setEnabled(false);
		setBottomMenu(bottomMenu);

		getWorkingArea().addComponent(generate());
	}

	private Component generate() {

		valueChangeOriginal = new ValueChangeOriginalXml();
		valueChangeProcessed = new ValueChangeProcessedXml();
		valueChangeResult = new ValueChangeResultXml();

		HorizontalLayout rootLayout = new HorizontalLayout();

		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		originalFiles = new TableXmlFiles();
		originalFiles.setSizeFull();
		originalFiles.setSelectable(true);
		originalFiles.addValueChangeListener(valueChangeOriginal);

		processedFiles = new TableXmlFiles();
		processedFiles.setSizeFull();
		processedFiles.setSelectable(true);
		processedFiles.addValueChangeListener(valueChangeProcessed);

		resultComparation = new TableResultComparation();
		resultComparation.setSizeFull();
		resultComparation.setSelectable(true);
		resultComparation.setWidth(RESULT_WIDTH);
		resultComparation.addValueChangeListener(valueChangeResult);

		comparationResult = new TextArea();
		comparationResult.setSizeFull();
		comparationResult.setReadOnly(true);

		rootLayout.addComponent(originalFiles);
		rootLayout.addComponent(processedFiles);
		rootLayout.addComponent(resultComparation);
		rootLayout.addComponent(comparationResult);

		rootLayout.setExpandRatio(originalFiles, 0.3f);
		rootLayout.setExpandRatio(processedFiles, 0.3f);
		rootLayout.setExpandRatio(comparationResult, 0.40f);

		return rootLayout;
	}

	protected void setComparationResult(String result) {
		comparationResult.setReadOnly(false);
		comparationResult.setValue(result);
		comparationResult.setReadOnly(true);
	}

	private UpperMenu createUpperMenu() {
		UpperMenu menu = new UpperMenu();

		menu.addCompareListener(new Button.ClickListener() {
			private static final long serialVersionUID = -5707519949745229833L;

			@Override
			public void buttonClick(ClickEvent event) {
				compareXmlContents();
			}
		});
		menu.addUploadOriginalListener(new Button.ClickListener() {
			private static final long serialVersionUID = -5707519949745229833L;

			@Override
			public void buttonClick(ClickEvent event) {
				uploadOriginalXml();
			}
		});
		menu.addUploadProcessedListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8830030428829864087L;

			@Override
			public void buttonClick(ClickEvent event) {
				uploadProcessedXml();
			}
		});
		menu.addRemoveListener(new Button.ClickListener() {
			private static final long serialVersionUID = -9091662409267978653L;

			@Override
			public void buttonClick(ClickEvent event) {
				originalFiles.removeValueChangeListener(valueChangeOriginal);
				processedFiles.removeValueChangeListener(valueChangeProcessed);

				originalFiles.removeCurrentSelection();
				processedFiles.removeCurrentSelection();

				originalFiles.addValueChangeListener(valueChangeOriginal);
				processedFiles.addValueChangeListener(valueChangeProcessed);
				
				resultComparation.setValue(null);
				resultComparation.removeAllItems();
			}
		});

		return menu;
	}

	protected void compareXmlContents() {
		if (originalFiles.getItemIds().size() != processedFiles.getItemIds().size()) {
			MessageManager
					.showWarning(LanguageCodes.WARNING_DESCRIPTION_NUMBER_OF_ORIGINAL_AND_PROCESSED_FILES_DOESNT_MATCH);
		}

		Iterator<?> itrI = originalFiles.getItemIds().iterator();
		Iterator<?> itrJ = processedFiles.getItemIds().iterator();

		resultComparation.removeAllItems();

		while (itrI.hasNext()) {
			UploadedFile originalFile = (UploadedFile) itrI.next();
			if (itrJ.hasNext()) {
				UploadedFile processedFile = (UploadedFile) itrJ.next();
				compareXmlContents(originalFile, processedFile);
			} else {
				break;
			}
		}
	}

	private void compareXmlContents(UploadedFile originalFile, UploadedFile processedFile) {
		CompareXmlToXml comparator = null;
		try {
			comparator = new CompareXmlToXml(originalFile.getStream(), processedFile.getStream());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR_IN_COMPARATION);
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		String message = "File '" + originalFile.getFileName() + "' and '" + processedFile.getFileName() + "' ";

		if (comparator.comparate()) {
			message += "contain the same information";
			resultComparation.addOk(message);
		} else {
			message += comparator.getCause();
			resultComparation.addError(message);
		}
	}

	protected void uploadOriginalXml() {
		final XmlWindowUpload uploadWindow = new XmlWindowUpload();
		uploadWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				List<UploadedFile> files = uploadWindow.getFiles();
				addXmlFiles(files, originalFiles);
				window.close();
			}
		});
		uploadWindow.showCentered();
	}

	protected void uploadProcessedXml() {
		final XmlWindowUpload uploadWindow = new XmlWindowUpload();
		uploadWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				List<UploadedFile> files = uploadWindow.getFiles();
				addXmlFiles(files, processedFiles);
				window.close();
			}
		});
		uploadWindow.showCentered();
	}

	protected void addXmlFiles(List<UploadedFile> files, TableXmlFiles table) {
		for (UploadedFile file : files) {
			table.addRow(file);
		}
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	public class ValueChangeOriginalXml implements ValueChangeListener {
		private static final long serialVersionUID = -6934095193340246239L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (event.getProperty().getValue() != null) {
				processedFiles.setValue(null);
				resultComparation.removeValueChangeListener(valueChangeResult);
				resultComparation.setValue(originalFiles.getSelectedPosition());
				resultComparation.addValueChangeListener(valueChangeResult);
			}else{
				resultComparation.removeValueChangeListener(valueChangeResult);
				resultComparation.setValue(null);
				resultComparation.addValueChangeListener(valueChangeResult);
			}
			updateProcessedText();
		}
	}

	public class ValueChangeProcessedXml implements ValueChangeListener {
		private static final long serialVersionUID = -7053035215659481939L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			if (event.getProperty().getValue() != null) {
				originalFiles.setValue(null);
				resultComparation.removeValueChangeListener(valueChangeResult);
				resultComparation.setValue(processedFiles.getSelectedPosition());
				resultComparation.addValueChangeListener(valueChangeResult);
			}else{
				resultComparation.removeValueChangeListener(valueChangeResult);
				resultComparation.setValue(null);
				resultComparation.addValueChangeListener(valueChangeResult);
			}
			updateProcessedText();
		}
	}

	public class ValueChangeResultXml implements ValueChangeListener {
		private static final long serialVersionUID = -4278522308125189132L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			originalFiles.removeValueChangeListener(valueChangeOriginal);
			processedFiles.removeValueChangeListener(valueChangeProcessed);
			if (event.getProperty().getValue() != null) {
				originalFiles.selectItemAtPosition((Integer) event.getProperty().getValue());
				processedFiles.selectItemAtPosition((Integer) event.getProperty().getValue());
			} else {
				originalFiles.setValue(null);
				processedFiles.setValue(null);
			}
			originalFiles.addValueChangeListener(valueChangeOriginal);
			processedFiles.addValueChangeListener(valueChangeProcessed);

			updateProcessedText();
		}
	}

	public void updateProcessedText() {
		if (resultComparation.getValue() != null) {
			String result = resultComparation.getSelectedResult();
			setComparationResult(result);
		}else{
			setComparationResult(new String());
		}
	}
}
