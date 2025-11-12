package com.biit.gui.tester;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
