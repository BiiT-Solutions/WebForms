package com.biit.webforms.gui.webpages;

import com.biit.usermanager.security.IActivity;
import com.biit.webforms.gui.WebformsUiLogger;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.compare.content.TableOriginalProcessedComparation;
import com.biit.webforms.gui.webpages.compare.content.UpperMenu;
import com.biit.webforms.gui.webpages.compare.structure.XmlWindowUpload;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.xml.CompareXmlToXml;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompareContent extends SecuredWebPage {
	private static final long serialVersionUID = 8670784419625264277L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(Arrays.asList(WebformsActivity.READ));

	private UpperMenu upperMenu;

	TableOriginalProcessedComparation tableXmlFiles;

	private TextArea comparationResult;

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

		HorizontalLayout rootLayout = new HorizontalLayout();

		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setSizeFull();

		tableXmlFiles = new TableOriginalProcessedComparation();
		tableXmlFiles.setSizeFull();
		tableXmlFiles.setSelectable(true);
		tableXmlFiles.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5829670383313278757L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					String result = tableXmlFiles.getResult(event.getProperty().getValue());
					setComparationResult(result);
				}
			}
		});

		comparationResult = new TextArea();
		comparationResult.setSizeFull();
		comparationResult.setReadOnly(true);

		rootLayout.addComponent(tableXmlFiles);
		rootLayout.addComponent(comparationResult);

		rootLayout.setExpandRatio(tableXmlFiles, 0.6f);
		rootLayout.setExpandRatio(comparationResult, 0.4f);

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
				updateResultOfComparation();
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
				tableXmlFiles.removeSelected();
				updateResultOfComparation();
			}
		});
		menu.addCleanListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6318807184713228031L;

			@Override
			public void buttonClick(ClickEvent event) {
				tableXmlFiles.setValue(null);
				tableXmlFiles.removeAllItems();
				updateResultOfComparation();
			}
		});

		return menu;
	}

	protected void updateResultOfComparation() {
		if (tableXmlFiles.getValue() != null) {
			String result = tableXmlFiles.getResult(tableXmlFiles.getValue());
			setComparationResult(result);
		} else {
			setComparationResult(new String());
		}
	}

	protected void compareXmlContents() {

		boolean missingFilesShown = false;
		for (Object itemId : tableXmlFiles.getItemIds()) {

			if (tableXmlFiles.isOriginalAndProcessedFileUploaded(itemId)) {
				compareXmlContents(itemId, tableXmlFiles.getOriginalFile(itemId), tableXmlFiles.getProcessedFile(itemId));
			} else {
				if (!missingFilesShown) {
					MessageManager.showWarning(LanguageCodes.WARNING_DESCRIPTION_NUMBER_OF_ORIGINAL_AND_PROCESSED_FILES_DOESNT_MATCH);
					missingFilesShown = true;
				}
			}

		}
	}

	private void compareXmlContents(Object itemId, UploadedFile originalFile, UploadedFile processedFile) {
		CompareXmlToXml comparator = null;
		try {
			comparator = new CompareXmlToXml(originalFile.getStream(), processedFile.getStream());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR_IN_COMPARATION);
			WebformsUiLogger.errorMessage(this.getClass().getName(), e);
		}

		String message = "File '" + originalFile.getFileName() + "' and '" + processedFile.getFileName() + "' ";

		if (comparator.comparate()) {
			message += "contain the same information";
			tableXmlFiles.addOk(itemId, message);
		} else {
			message += comparator.getCause();
			tableXmlFiles.addError(itemId, message);
		}
	}

	protected void uploadOriginalXml() {
		final XmlWindowUpload uploadWindow = new XmlWindowUpload();
		uploadWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				List<UploadedFile> files = uploadWindow.getFiles();
				addOriginalXmlFiles(files);
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
				addProcessedXmlFiles(files);
				window.close();
			}
		});
		uploadWindow.showCentered();
	}

	protected void addOriginalXmlFiles(List<UploadedFile> files) {
		for (UploadedFile file : files) {
			tableXmlFiles.addRow(file, null);
		}
	}

	protected void addProcessedXmlFiles(List<UploadedFile> files) {
		for (UploadedFile file : files) {
			tableXmlFiles.addRow(null, file);
		}
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}
}
