package com.biit.webforms.gui.xforms;

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class OrbeonPreview extends VerticalLayout implements View {
	private static final long serialVersionUID = -4957704029911500631L;
	private Panel mainPanel;

	public void createBrowser(String application, String form) {
		BrowserFrame browser = new BrowserFrame("", new ExternalResource(WebformsConfigurationReader.getInstance()
				.getOrbeonFormRunnerUrl() + "/" + application + "/" + form + "/new"));
		browser.setSizeFull();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.addComponent(browser);
		rootLayout.setSizeFull();

		mainPanel.setContent(rootLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		mainPanel = new Panel();

		addComponent(mainPanel);
		setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);
		setSizeFull();

		mainPanel.setSizeFull();
	}

}
