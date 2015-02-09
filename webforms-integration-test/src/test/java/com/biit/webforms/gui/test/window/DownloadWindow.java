package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.biit.webforms.gui.test.exceptions.IncorrectFileGenerationException;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.WindowElement;

public class DownloadWindow extends VaadinGuiWindow {

	public static final String DOWNLOAD_BUTTON_CAPTION = "Download file";
	public static final String FILE_GENERATION_SUCESSFUL_LABEL = "The file has been generated successfully!";
	public static final String WINDOW_CAPTION = "Download file.";

	private LabelElement getDownloadMessage() {
		$(LabelElement.class).first().waitForVaadin();
		return $(LabelElement.class).first();
	}

	private boolean isDownloadButtonPresent() {
		return $(ButtonElement.class).caption(DOWNLOAD_BUTTON_CAPTION).exists();
	}

	public void checkCorrectFileGeneration() throws IncorrectFileGenerationException {
		if (!getDownloadMessage().getText().equals(FILE_GENERATION_SUCESSFUL_LABEL) || !isDownloadButtonPresent()) {
			throw new IncorrectFileGenerationException();
		}
	}
	
	public void closeWindow(){
		$$(WindowElement.class).caption(WINDOW_CAPTION).first().findElement(By.className("v-window-closebox")).click();
	}

}
