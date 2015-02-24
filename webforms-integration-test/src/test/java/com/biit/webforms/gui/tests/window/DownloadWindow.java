package com.biit.webforms.gui.tests.window;

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiWindow;
import com.biit.webforms.gui.tests.exceptions.IncorrectFileGenerationException;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elements.WindowElement;

public class DownloadWindow extends VaadinGuiWindow {

	public static final String DOWNLOAD_BUTTON_CAPTION = "Download file";
	public static final String FILE_GENERATION_SUCESSFUL_LABEL = "The file has been generated successfully!";
	public static final String WINDOW_CAPTION = "Download file.";
	public static final Integer SLEEP_TIME = 3000;
	private static final String WINDOW_CLOSE_BUTTON_CLASS_NAME = "v-window-closebox";
	private static final String CLASS_NAME = "com.biit.webforms.gui.common.components.WindowDownloader";

	private boolean isDownloadMessageCorrect() {
		if (getWindow().$(LabelElement.class).exists()
				&& $(LabelElement.class).first().getText().equals(FILE_GENERATION_SUCESSFUL_LABEL)) {
			return true;
		} else if (getWindow().$$(VerticalLayoutElement.class).$$(LabelElement.class).exists()
				&& getWindow().$$(VerticalLayoutElement.class).$$(LabelElement.class).first().getText()
						.equals(FILE_GENERATION_SUCESSFUL_LABEL)) {
			return true;
		}
		return false;
	}

	private boolean isDownloadButtonPresent() {
		return getWindow().$(ButtonElement.class).caption(DOWNLOAD_BUTTON_CAPTION).exists();
	}

	public void checkCorrectFileGeneration() throws IncorrectFileGenerationException {
		// We have to wait until the window is created
		waitToShow();
		try {
			// We have to wait until the message and the button are created (or
			// not)
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			throw new IncorrectFileGenerationException();
		}
		if (!isDownloadMessageCorrect() || !isDownloadButtonPresent()) {
			throw new IncorrectFileGenerationException();
		}
	}

	public void closeWindow() {
		getWindow().findElement(By.className(WINDOW_CLOSE_BUTTON_CLASS_NAME)).click();
	}

	public WindowElement getWindow() {
		try {
			return $$(WindowElement.class).id(getWindowId());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void waitToShow() {
		while (true) {
			try {
				if (getWindow() != null) {
					return;
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}

	protected String getWindowId() {
		return CLASS_NAME;
	}

}
