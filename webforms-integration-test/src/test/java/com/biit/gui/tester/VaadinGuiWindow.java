package com.biit.gui.tester;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.vaadin.testbench.TestBenchTestCase;

public class VaadinGuiWindow extends TestBenchTestCase {

	public void sendKeyDown(Keys key) {
		Actions builder = new Actions(getDriver());
		builder.keyDown(key).perform();
	}

	public void sendKeyUp(Keys key) {
		Actions builder = new Actions(getDriver());
		builder.keyUp(key).perform();
	}

	public void pressKeys(Keys... keys) {
		Actions builder = new Actions(getDriver());
		builder.sendKeys(keys).perform();
	}
}
