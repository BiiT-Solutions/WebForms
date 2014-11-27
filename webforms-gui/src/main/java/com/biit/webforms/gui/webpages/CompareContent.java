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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;

public class CompareContent extends SecuredWebPage {
	private static final long serialVersionUID = 8670784419625264277L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private UpperMenu upperMenu;

	private TableXmlFiles originalFiles;
	private TableXmlFiles processedFiles;

	private TableResultComparation resultComparation;

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

		originalFiles = new TableXmlFiles();
		originalFiles.setSizeFull();
		originalFiles.setSelectable(true);

		processedFiles = new TableXmlFiles();
		processedFiles.setSizeFull();
		processedFiles.setSelectable(true);

		resultComparation = new TableResultComparation();
		resultComparation.setSizeFull();
		resultComparation.setSelectable(true);

		comparationResult = new TextArea();
		comparationResult.setSizeFull();
		comparationResult.setReadOnly(true);

		rootLayout.addComponent(originalFiles);
		rootLayout.addComponent(processedFiles);
		rootLayout.addComponent(resultComparation);
		rootLayout.addComponent(comparationResult);

		return rootLayout;
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
		
		String message = "File '" + originalFile.getFileName() + "' and '" + processedFile.getFileName()
				+ "' ";
		
		if (comparator.comparate()) {
			message +="contain the same information";
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

}
