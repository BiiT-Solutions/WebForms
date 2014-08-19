package com.biit.webforms.gui.common.components;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class WebPage extends WebPageComponent {
	private static final long serialVersionUID = 5807470662051568616L;
	private AbstractOrderedLayout workingArea;

	public WebPage() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		workingArea = getRootLayout();
		setCompositionRoot(getRootLayout());
		setSizeFull();
	}

	public void addCentralPanel() {
		Panel mainPanel = new Panel();
		getRootLayout().addComponent(mainPanel);
		getRootLayout().setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);
		getRootLayout().setExpandRatio(mainPanel, 1.0f);
		mainPanel.setSizeFull();

		workingArea = new VerticalLayout();
		workingArea.setMargin(false);
		workingArea.setSpacing(false);
		workingArea.setSizeFull();

		mainPanel.setContent(workingArea);
		mainPanel.setSizeFull();
	}

	/**
	 * Returns Working Area layout where components should be displayed.
	 * 
	 * @return
	 */
	public AbstractOrderedLayout getWorkingArea() {
		return workingArea;
	}

	/**
	 * Clean all the page content
	 */
	public void cleanPage() {
		getRootLayout().removeAllComponents();
		workingArea = getRootLayout();
	}

	/**
	 * Clean only the working area
	 */
	public void cleanWorkingArea() {
		getWorkingArea().removeAllComponents();
	}
}
