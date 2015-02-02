package com.biit.gui.tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;

public abstract class VaadinGuiWebpage extends TestBenchTestCase {

	private final List<VaadinGuiWindow> windows;
	private final List<VaadinGuiView> views;
	private static final String ACCEPT_BUTTON_CAPTION = "Accept";

	public VaadinGuiWebpage() {
		windows = new ArrayList<VaadinGuiWindow>();
		views = new ArrayList<VaadinGuiView>();
	}

	public void addWindow(VaadinGuiWindow window) {
		windows.add(window);
	}

	public void addView(VaadinGuiView view) {
		views.add(view);
	}

	@Override
	public void setDriver(WebDriver driver) {
		super.setDriver(driver);
		for (VaadinGuiWindow window : windows) {
			window.setDriver(getDriver());
		}
		for (VaadinGuiView view : views) {
			view.setDriver(getDriver());
		}
	}

	public void goToPage() {
		getDriver().get(getWebpageUrl());
	}

	public abstract String getWebpageUrl();

	public ButtonElement getAcceptButton() {
		if ($(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).exists()) {
			return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
		} else {
			return null;
		}
	}

	public void clickAcceptButtonIfExists() {
		if (getAcceptButton() != null) {
			getAcceptButton().click();
		}
	}

	public ButtonElement getButtonElement(String buttonCaption) {
		return $(ButtonElement.class).caption(buttonCaption).first();
	}

	/**
	 * @param buttonCaption
	 * @return <code>true</code> if the button exists.<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean existsButton(String buttonCaption) {
		return $(ButtonElement.class).caption(buttonCaption).exists();
	}

}
