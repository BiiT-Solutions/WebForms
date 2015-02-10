package com.biit.webforms.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.biit.webforms.gui.test.exceptions.IncorrectFileGenerationException;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

public class DownloadWindow extends VaadinGuiWindow {

	public static final String DOWNLOAD_BUTTON_CAPTION = "Download file";
	public static final String FILE_GENERATION_SUCESSFUL_LABEL = "The file has been generated successfully!";
	public static final String WINDOW_CAPTION = "Download file.";
	public static final Integer SLEEP_TIME = 2000;
	private static final String WINDOW_CLOSE_BUTTON_CLASS_NAME = "v-window-closebox";

	private boolean isDownloadMessageCorrect() {
		if ($(LabelElement.class).exists()
				&& $(LabelElement.class).first().getText().equals(FILE_GENERATION_SUCESSFUL_LABEL)) {
			return true;
		} else if ($$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
				.$$(LabelElement.class).exists()
				&& $$(WindowElement.class).caption(WINDOW_CAPTION).$$(VerticalLayoutElement.class)
						.$$(LabelElement.class).first().getText().equals(FILE_GENERATION_SUCESSFUL_LABEL)) {
			return true;
		}
		return false;
	}

	private boolean isDownloadButtonPresent() {
		return $(ButtonElement.class).caption(DOWNLOAD_BUTTON_CAPTION).exists();
	}

	public void checkCorrectFileGeneration() throws IncorrectFileGenerationException {
		while (!($$(WindowElement.class).caption(WINDOW_CAPTION).exists())) {
			// Do nothing
			// We have to wait until the window is created
		}
		try {
			// We have to wait until the message and the button are created (or
			// not)
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!isDownloadMessageCorrect() || !isDownloadButtonPresent()) {
			throw new IncorrectFileGenerationException();
		}
	}

	public void closeWindow() {
		$$(WindowElement.class).caption(WINDOW_CAPTION).first().findElement(By.className(WINDOW_CLOSE_BUTTON_CLASS_NAME)).click();
	}

}
