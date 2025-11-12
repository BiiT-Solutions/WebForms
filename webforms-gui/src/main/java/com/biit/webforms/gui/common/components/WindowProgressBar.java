package com.biit.webforms.gui.common.components;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import com.vaadin.ui.*;

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
