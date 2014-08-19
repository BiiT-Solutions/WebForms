package com.biit.webforms.gui.common.components;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class WebPage extends WebPageComponent {
	private static final long serialVersionUID = 5807470662051568616L;
	private AbstractOrderedLayout workingArea;
	private UpperMenu upperMenu;

	public WebPage() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		workingArea = getRootLayout();
		setCompositionRoot(getRootLayout());
		setSizeFull();
	}

	public void setAsCentralPanel() {
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

	public void setUpperMenu(UpperMenu upperMenu) {
		if (this.upperMenu != null) {
			this.getRootLayout().removeComponent(this.upperMenu);
		}
		this.upperMenu = upperMenu;
		this.getRootLayout().addComponent(upperMenu, 0);
		getRootLayout().setComponentAlignment(upperMenu, Alignment.BOTTOM_CENTER);
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
