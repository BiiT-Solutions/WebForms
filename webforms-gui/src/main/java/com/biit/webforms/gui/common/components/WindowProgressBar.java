package com.biit.webforms.gui.common.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowProgressBar extends Window {
	private static final long serialVersionUID = 9010880290106574411L;
	private static final String WIDTH = "310px";
	private static final String HEIGHT = "150px";
	private ProgressBar progressBar;

	public WindowProgressBar(String caption) {
		super();
		setId(this.getClass().getName());
		progressBar = new ProgressBar();
		setCaption(caption);
		setIndeterminate(true);
	}
	
	public void configure() {
		setModal(true);
		setClosable(true);
		setResizable(false);
		setWidth(WIDTH);
		setHeight(HEIGHT);
	}

	public void showCentered() {
		center();
		UI.getCurrent().addWindow(this);
	}

	public void process(String message) {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSizeFull();

		Label messageLabel = new Label();
		messageLabel.setWidth(null);
		messageLabel.setValue(message);
		rootLayout.addComponent(messageLabel);
		rootLayout.setComponentAlignment(messageLabel, Alignment.MIDDLE_LEFT);
		rootLayout.setExpandRatio(messageLabel, 0.30f);

		rootLayout.addComponent(progressBar);
		rootLayout.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
		rootLayout.setExpandRatio(progressBar, 0.70f);

		setContent(rootLayout);
	}

	/**
	 * Makes visualization of progress bar as a undetermined clock.
	 * 
	 * @param value
	 */
	public void setIndeterminate(boolean value) {
		progressBar.setIndeterminate(value);
	}

	public void setProgress(float value) {
		progressBar.setValue(value);
	}

	/**
	 * Method to update Ui progress while working
	 */
	protected synchronized void updateProgress(final float value) {
		// Update the UI thread-safely
		UI.getCurrent().access(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(value);
			}
		});
	}

}
