package com.biit.gui.tester;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class VaadinGuiWebpage extends TestBenchTestCase {

    private static final String SCREENSHOT_TYPE = ".png";

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
        sleep(500);
        if (getAcceptButton() != null) {
            getAcceptButton().click();
        }
    }

    public ButtonElement getButtonElement(String buttonCaption) {

        List<ButtonElement> buttons = $(ButtonElement.class).caption(buttonCaption).all();
        if (buttons.isEmpty()) {
            return null;
        } else {
            return buttons.get(0);
        }
    }

    /**
     * @param buttonCaption
     * @return <code>true</code> if the button exists.<br>
     * <code>false</code> otherwise.
     */
    public boolean existsButton(String buttonCaption) {
        return $(ButtonElement.class).caption(buttonCaption).exists();
    }

    public CheckBoxElement getCheckBoxByCaption(String caption) {
        return $(CheckBoxElement.class).caption(caption).first();
    }

    /**
     * Takes a screenshot of the webpage and stores it in the Temp folder
     *
     * @param screenshotName
     */
    public void takeScreenshot(String screenshotName) {
        File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(System.getProperty("java.io.tmpdir") + File.separator + screenshotName + SCREENSHOT_TYPE), true);
        } catch (IOException ignored) {
        }
    }

    protected void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
