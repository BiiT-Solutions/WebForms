package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.xml.XmlUtils;
import com.biit.webforms.xsd.WebformsXsdForm;

import java.io.InputStream;

public class WindowDownloaderXsd extends WindowDownloader {

	private static final long serialVersionUID = -847080376303508647L;

	public WindowDownloaderXsd(final Form form, String filename) {
		super(new WindowDownloaderProcess() {

			@Override
			public InputStream getInputStream() {
				return XmlUtils.formatToInputStream(new WebformsXsdForm(form).toString());
			}
		});
		setIndeterminate(true);
		setFilename(filename);
		showCentered();
	}
}
