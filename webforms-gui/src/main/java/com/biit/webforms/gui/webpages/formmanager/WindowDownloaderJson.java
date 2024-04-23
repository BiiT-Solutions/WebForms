package com.biit.webforms.gui.webpages.formmanager;

import com.biit.webforms.gui.common.components.WindowDownloader;
import com.biit.webforms.gui.common.components.WindowDownloaderProcess;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WindowDownloaderJson extends WindowDownloader {
    private static final long serialVersionUID = 1327278107256149670L;

    public WindowDownloaderJson(final CompleteFormView completeFormView, String filename) {
        super(new WindowDownloaderProcess() {

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(completeFormView.toJson().getBytes(StandardCharsets.UTF_8));
            }
        });
        setIndeterminate(true);
        setFilename(filename);
        showCentered();
    }

    public WindowDownloaderJson(final Form form, String filename) {
        super(new WindowDownloaderProcess() {

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(form.toJson().getBytes(StandardCharsets.UTF_8));
            }
        });
        setIndeterminate(true);
        setFilename(filename);
        showCentered();
    }
}