package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.gui.webpages.formmanager.decisiontable.SaveFormToCsvAction;
import com.biit.webforms.persistence.entity.CompleteFormView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class WindowDownloaderCsv extends WindowDownloader {
    private static final long serialVersionUID = 1327278107256149670L;

    public WindowDownloaderCsv(final CompleteFormView completeFormView, String filename) {
        super(new WindowDownloaderProcess() {

            @Override
            public InputStream getInputStream() {
                byte [] b = new SaveFormToCsvAction(completeFormView).getInformationData();
                return new ByteArrayInputStream(b);
            }
        });
        setIndeterminate(true);
        setFilename(filename);
        showCentered();
    }
}
