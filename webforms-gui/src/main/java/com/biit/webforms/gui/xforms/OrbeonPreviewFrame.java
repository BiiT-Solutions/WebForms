package com.biit.webforms.gui.xforms;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Frame to open a preview of an Orbeon form in a new window.
 */
public class OrbeonPreviewFrame extends UI {
	public final static String FORM_PARAMETER_TAG = "form_param";
	public final static String APPLICATION_PARAMETER_TAG = "application_param";
	private static final long serialVersionUID = -4957704029911500631L;
	private Navigator navigator;
	private String form;
	private String application;

	public String getApplication() {
		return application;
	}

	public String getForm() {
		return form;
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Preview");
		this.form = request.getParameter(FORM_PARAMETER_TAG);
		this.application = request.getParameter(APPLICATION_PARAMETER_TAG);
		// Create a navigator to control the views
		navigator = new Navigator(this, this);

		navigator.addViewChangeListener(new ViewChangeListener() {
			private static final long serialVersionUID = -668206181478591694L;

			@Override
			public void afterViewChange(ViewChangeEvent event) {
				OrbeonPreview previewer = (OrbeonPreview) event.getNewView();
				previewer.createBrowser(getApplication(), getForm());
			}

			@Override
			public boolean beforeViewChange(ViewChangeEvent event) {
				return true;
			}
		});

		navigator.addView("ORBEON_PREVIEW", OrbeonPreview.class);
		UI.getCurrent().getNavigator().navigateTo("ORBEON_PREVIEW");
	}

}
