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

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.NotificationElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.List;

@Listeners({TestListener.class})
public class VaadinGuiTester extends TestBenchTestCase {

    private static final String FIREFOX_LANGUAGE_PROPERTY = "intl.accept_languages";
    private static final String FIREFOX_LANGUAGE_VALUE = "en_US";
    private static final String APPLICATION_URL_NEW_UI = "http://localhost:9081/?restartApplication";
    private static final String NOTIFICATION_TYPE_HUMANIZED = "humanized";
    private static final String NOTIFICATION_TYPE_WARNING = "warning";
    private static final String NOTIFICATION_TYPE_ERROR = "error";
    private static final Integer WIDTH = 1920;
    private static final Integer HEIGHT = 1080;

    // This parameter set to 'true' activates phantomJs driver instead of
    // firefox driver
    private boolean headlessTesting = false;

    // To debug last step on firefox
    private boolean destroyDriver = true;


    private final List<VaadinGuiWebpage> webpages;

    public VaadinGuiTester() {
        webpages = new ArrayList<>();
    }

    @BeforeClass(inheritGroups = true, alwaysRun = true)
    public void createDriver(ITestContext iTestContext) {
        if (headlessTesting) {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setJavascriptEnabled(true);
            caps.setCapability("takesScreenshot", true);
            caps.setCapability("phantomjs.page.customHeaders.Accept-Language", FIREFOX_LANGUAGE_VALUE);
            setDriver(TestBench.createDriver(new PhantomJSDriver(caps)));
        } else {
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference(FIREFOX_LANGUAGE_PROPERTY, FIREFOX_LANGUAGE_VALUE);
            setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
        }
        getDriver().manage().window().setSize(new Dimension(WIDTH, HEIGHT));
        for (VaadinGuiWebpage webpage : webpages) {
            webpage.setDriver(getDriver());
        }

        iTestContext.setAttribute("driver", driver);
    }

    @AfterClass(inheritGroups = true, alwaysRun = true)
    public void destroyDriver() {
        if (destroyDriver) {
            try {
                getDriver().quit();
            } catch (NullPointerException npe) {
                // Already destroyed.
            }
        }
    }

    public void addWebpage(VaadinGuiWebpage webpage) {
        webpages.add(webpage);
    }

    public void mainPage() {
        getDriver().get(APPLICATION_URL_NEW_UI);
    }

    public NotificationElement getNotification() {
        return $(NotificationElement.class).first();
    }

    public void closeNotificationIfExists() {
        if ($(NotificationElement.class).exists()) {
            $(NotificationElement.class).first().close();
        }
    }

    public static void closeNotification(NotificationElement notification) {
        try {
            try {
                notification.waitForVaadin();
                notification.close();
            } catch (TimeoutException e) {
                // Do nothing.
            }
        } catch (StaleElementReferenceException e) {
            // Do nothing
        }
    }

    public static void checkNotificationIsError(NotificationElement notification) {
        Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_ERROR);
        closeNotification(notification);
    }

    public static void checkNotificationIsWarning(NotificationElement notification) {
        Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_WARNING);
        closeNotification(notification);
    }

    public static void checkNotificationIsHumanized(NotificationElement notification) {
        Assert.assertEquals(notification.getType(), NOTIFICATION_TYPE_HUMANIZED);
        closeNotification(notification);
    }

    public boolean isHeadlessTesting() {
        return headlessTesting;
    }
}
