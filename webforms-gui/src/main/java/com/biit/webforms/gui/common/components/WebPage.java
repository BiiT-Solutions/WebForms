package com.biit.webforms.gui.common.components;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class WebPage extends WebPageComponent {
	private static final long serialVersionUID = 5807470662051568616L;

	private AbstractOrderedLayout workingArea;

	private UpperMenu upperMenu;
	private Panel centralPanel;
	private BottomMenu bottomMenu;

	private enum WebPageElements {
		UPPER_MENU, CENTRAL_AREA, BOTTOM_MENU
	};

	public WebPage() {
		setRootLayout(new VerticalLayout());
		getRootLayout().setSizeFull();
		workingArea = getRootLayout();
		setCompositionRoot(getRootLayout());
		setSizeFull();
	}

	public void setCentralPanelAsWorkingArea() {
		centralPanel = new Panel();
		getRootLayout().addComponent(centralPanel, calculateElementPosition(WebPageElements.CENTRAL_AREA));
		getRootLayout().setComponentAlignment(centralPanel, Alignment.MIDDLE_CENTER);
		getRootLayout().setExpandRatio(centralPanel, 1.0f);
		centralPanel.setSizeFull();

		workingArea = new VerticalLayout();
		workingArea.setMargin(false);
		workingArea.setSpacing(false);
		workingArea.setSizeFull();

		centralPanel.setContent(workingArea);
		centralPanel.setSizeFull();
	}

	public void setUpperMenu(UpperMenu upperMenu) {
		clearUpperMenu();
		if (upperMenu != null) {
			this.upperMenu = upperMenu;
			this.getRootLayout().addComponent(upperMenu, 0);
			getRootLayout().setComponentAlignment(upperMenu, Alignment.TOP_CENTER);
		}
	}

	public void setBottomMenu(BottomMenu bottomMenu) {
		clearBottomMenu();
		if (bottomMenu != null) {
			this.bottomMenu = bottomMenu;
			this.getRootLayout().addComponent(bottomMenu);
			getRootLayout().setComponentAlignment(bottomMenu, Alignment.BOTTOM_CENTER);
		}
	}

	protected int calculateElementPosition(WebPageElements element) {
		int position = 0;
		switch (element) {
		case UPPER_MENU:
			return 0;
		case CENTRAL_AREA:
			if (upperMenu != null) {
				position++;
			}
			return position;
		case BOTTOM_MENU:
			if (upperMenu != null) {
				position++;
			}
			if (centralPanel != null) {
				position++;
			}
			return position;
		}
		return position;
	}

	/**
	 * Removes upper menu from visualization and data.
	 */
	public void clearUpperMenu() {
		if (upperMenu != null) {
			this.getRootLayout().removeComponent(upperMenu);
			upperMenu = null;
		}
	}

	/**
	 * Removes bottom menu from visualization and data.
	 */
	public void clearBottomMenu() {
		if (bottomMenu != null) {
			this.getRootLayout().removeComponent(bottomMenu);
			bottomMenu = null;
		}
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
