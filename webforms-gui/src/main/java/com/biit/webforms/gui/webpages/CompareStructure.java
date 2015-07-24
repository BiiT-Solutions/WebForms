package com.biit.webforms.gui.webpages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.biit.form.validators.ValidateBaseForm;
import com.biit.usermanager.security.IActivity;
import com.biit.webforms.gui.UserSessionHandler;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.UploadedFile;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.webpages.compare.structure.TableXmlFiles;
import com.biit.webforms.gui.webpages.compare.structure.UpperMenu;
import com.biit.webforms.gui.webpages.compare.structure.XmlWindowUpload;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.security.WebformsActivity;
import com.biit.webforms.xml.XmlUtils;
import com.biit.webforms.xsd.WebformsXsdForm;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class CompareStructure extends SecuredWebPage {
	private static final long serialVersionUID = 6637808831858543345L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));

	private static final float TABLE_WIDTH_EXPAND_RATIO = 0.25f;
	private static final float FILE_CONTENT_HEIGHT_RATIO = 0.75f;

	private UpperMenu upperMenu;
	private TextArea fileContent;
	private TextArea fileResult;

	private TableXmlFiles tableFiles;

	@Override
	protected void initContent() {
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());
		getWorkingArea().addComponent(generate());
	}

	private Component generate() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		tableFiles = new TableXmlFiles();
		tableFiles.setSizeFull();
		tableFiles.setImmediate(true);
		tableFiles.setSelectable(true);
		tableFiles.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8400674255105486484L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue() != null) {
					updateXmlFileContent(tableFiles.getXmlText((UploadedFile) tableFiles.getValue()));
					updateResultContent(tableFiles.getResult((UploadedFile) tableFiles.getValue()));
				} else {
					updateXmlFileContent("");
					updateResultContent("");
				}
			}
		});

		VerticalLayout fileContentAndCheck = new VerticalLayout();
		fileContentAndCheck.setSizeFull();
		fileContentAndCheck.setSpacing(true);

		fileContent = new TextArea();
		fileContent.setReadOnly(true);
		fileContent.setSizeFull();
		fileContent.setCaption(LanguageCodes.CAPTION_FILE_CONTENT.translation());
		fileResult = new TextArea();
		fileResult.setReadOnly(true);
		fileResult.setSizeFull();
		fileResult.setCaption(LanguageCodes.CAPTION_FILE_RESULT.translation());

		fileContentAndCheck.addComponent(fileContent);
		fileContentAndCheck.addComponent(fileResult);
		fileContentAndCheck.setExpandRatio(fileContent, FILE_CONTENT_HEIGHT_RATIO);
		fileContentAndCheck.setExpandRatio(fileResult, 1.0f - FILE_CONTENT_HEIGHT_RATIO);

		rootLayout.addComponent(tableFiles);
		rootLayout.addComponent(fileContentAndCheck);
		rootLayout.setExpandRatio(tableFiles, TABLE_WIDTH_EXPAND_RATIO);
		rootLayout.setExpandRatio(fileContentAndCheck, 1.0f - TABLE_WIDTH_EXPAND_RATIO);

		return rootLayout;
	}

	protected void updateResultContent(String result) {
		fileResult.setReadOnly(false);
		fileResult.setValue(result);
		fileResult.setReadOnly(true);
	}

	protected void updateXmlFileContent(String xmlText) {
		fileContent.setReadOnly(false);
		fileContent.setValue(xmlText);
		fileContent.setReadOnly(true);
	}

	private UpperMenu createUpperMenu() {
		UpperMenu upperMenu = new UpperMenu();

		upperMenu.addUploadListener(new ClickListener() {
			private static final long serialVersionUID = 9212765106458132972L;

			@Override
			public void buttonClick(ClickEvent event) {
				upload();
			}
		});
		upperMenu.addRemoveListener(new ClickListener() {
			private static final long serialVersionUID = 9212765106458132972L;

			@Override
			public void buttonClick(ClickEvent event) {
				remove();
			}
		});
		upperMenu.addRemoveAllListener(new ClickListener() {
			private static final long serialVersionUID = 9212765106458132972L;

			@Override
			public void buttonClick(ClickEvent event) {
				removeAll();
			}
		});
		upperMenu.addValidateListener(new ClickListener() {
			private static final long serialVersionUID = 9212765106458132972L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (tableFiles.getValue() != null) {
					validate((UploadedFile) tableFiles.getValue());
					updateResultContent(tableFiles.getResult((UploadedFile) tableFiles.getValue()));
				} else {
					MessageManager.showWarning(LanguageCodes.WARNING_VALIDATE_XML_STRUCTURE_NOT_SELECTED);
				}

			}
		});
		upperMenu.addValidateAllListener(new ClickListener() {
			private static final long serialVersionUID = 9212765106458132972L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateAll();
				if (tableFiles.getValue() != null) {
					updateResultContent(tableFiles.getResult((UploadedFile) tableFiles.getValue()));
				}
			}
		});

		return upperMenu;
	}

	protected void upload() {
		final XmlWindowUpload uploadWindow = new XmlWindowUpload();
		uploadWindow.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				List<UploadedFile> files = uploadWindow.getFiles();
				addFiles(files);
				window.close();
			}
		});
		uploadWindow.showCentered();
	}

	public void addFiles(List<UploadedFile> files) {
		for (UploadedFile file : files) {
			tableFiles.addRow(file);
		}
	}

	protected void remove() {
		if (tableFiles.getValue() != null) {
			tableFiles.removeRow((UploadedFile) tableFiles.getValue());
		}
	}

	protected void removeAll() {
		tableFiles.removeAllItems();
	}

	protected void validateAll() {
		for (Object itemId : tableFiles.getItemIds()) {
			validate((UploadedFile) itemId);
		}
	}

	private boolean validate(UploadedFile itemId) {
		String xsd = generateCurrentXsd();
		if (xsd != null) {
			String xml = tableFiles.getXmlText(itemId);
			try {
				String validationResult = XmlUtils.validateXml(xml, xsd);
				if (validationResult.isEmpty()) {
					tableFiles.setResult(itemId, LanguageCodes.XSD_XML_VALIDATION_ALL_OK.translation());
					tableFiles.setStatusMark(itemId, true);
					return true;
				} else {
					tableFiles.setResult(itemId, validationResult);
					tableFiles.setStatusMark(itemId, false);
					return false;
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				WebformsLogger.errorMessage(this.getClass().getName(), e);
			}
		}
		tableFiles.setResult(itemId, LanguageCodes.XSD_XML_VALIDATION_COULD_NOT_BE_DONE.translation());
		tableFiles.setStatusMark(itemId, false);
		return false;
	}

	private boolean isValidCurrentForm() {
		ValidateBaseForm validator = new ValidateBaseForm();
		return validator.validate(UserSessionHandler.getController().getCompleteFormView());
	}

	private String generateCurrentXsd() {
		if (isValidCurrentForm()) {
			return new WebformsXsdForm(UserSessionHandler.getController().getCompleteFormView()).toString();
		}
		MessageManager.showError(LanguageCodes.ERROR_MESSAGE_CURRENT_FORM_STRUCTURE_IS_NOT_VALID);
		return null;
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

}
