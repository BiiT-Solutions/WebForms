package com.biit.webforms.gui.webpages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.vaadin.risto.stepper.IntStepper;

import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.exporters.xml.XmlExporter;
import com.biit.webforms.utils.exporters.xml.exceptions.ElementWithoutNextElement;
import com.biit.webforms.utils.exporters.xml.exceptions.TooMuchIterationsWhileGeneratingPath;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WindowGenerateXml extends WindowAcceptCancel {
	private static final long serialVersionUID = -3167579242870259658L;
	private static final String WINDOW_WIDTH = "350px";
	private static final String WINDOW_HEIGHT = "240px";
	private Form form;
	private IntStepper stepper;

	public WindowGenerateXml(Form form) {
		super();
		configure();
		setContent(generateContent());
		setValue(form);
	}

	private Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		Label label = new Label(LanguageCodes.CAPTION_SELECT_AMMOUNT_OF_XML_TO_GENERATE.translation());
		label.setWidth(null);

		stepper = new IntStepper();
		stepper.setValue(1);
		stepper.setMinValue(1);

		rootLayout.addComponent(label);
		rootLayout.addComponent(stepper);

		rootLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
		rootLayout.setComponentAlignment(stepper, Alignment.MIDDLE_CENTER);

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
				exportXml();
			}
		});
	}

	protected void exportXml() {
		if (stepper.getValue() <= 0) {
			MessageManager.showWarning(LanguageCodes.WARNING_NUMBER_OF_GENERATED_XML_NOT_VALID);
			return;
		}

		WindowDownloader downloader = new WindowDownloader(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				try {
					XmlExporter exporter = new XmlExporter(form);
					List<String> xmlFiles = exporter.generate(stepper.getValue());

					byte[] zipFile = zipFiles(xmlFiles);					

					return new ByteArrayInputStream(zipFile);
				} catch (IOException | BadFormedExpressions | ElementWithoutNextElement
						| TooMuchIterationsWhileGeneratingPath e) {
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
	
	private byte[] zipFiles(List<String> xmlFiles) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		int numberOfFiles = xmlFiles.size();
		int digits = (int)(Math.log10(numberOfFiles)+1);		
		
		for(int i=0; i<xmlFiles.size();i++){
			String currentFile = String.format("%0"+digits+"d", i);
			addFileToZip(zos, xmlFiles.get(i).getBytes(Charset.forName("UTF-8")), "export_"+currentFile+".xml");
		}

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
		return form.getLabel() + ".zip";
	}
}
