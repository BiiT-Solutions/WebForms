package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.WindowTreeObject;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.theme.ThemeIcons;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;

public class WindowNewRule extends WindowAcceptCancel {
	private static final long serialVersionUID = 5164868235165988674L;
	private static final String width = "800px";
	private static final String height = "600px";

	private IconButton searchOrigin;
	private Label originName;
	private TreeObject origin;

	public WindowNewRule() {
		super();
		setContent(generateContent());
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public Component generateContent() {
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		RichTextArea richTextArea = new RichTextArea();
		richTextArea.setSizeFull();
		richTextArea.addStyleName("richTextArea-noControls");

		Component selectOriginComponent = generateSearchOriginContent();
		
		rootLayout.addComponent(selectOriginComponent);
		rootLayout.addComponent(richTextArea);
		rootLayout.setExpandRatio(selectOriginComponent, 0.1f);
		rootLayout.setExpandRatio(richTextArea, 0.9f);
		return rootLayout;
	}

	public Component generateSearchOriginContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setSpacing(true);

		searchOrigin = new IconButton(LanguageCodes.CAPTION_SEARCH_ORIGIN, ThemeIcons.SEARCH,
				LanguageCodes.TOOLTIP_SEARCH_ORIGIN);
		searchOrigin.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7638381994093082583L;

			@Override
			public void buttonClick(ClickEvent event) {
				openSelectOrigin();
			}
		});
		originName = new Label();

		rootLayout.addComponent(searchOrigin);
		rootLayout.addComponent(originName);

		return rootLayout;
	}

	protected void openSelectOrigin() {
		final WindowTreeObject windowTreeObject = new WindowTreeObject(
				LanguageCodes.CAPTION_WINDOW_SELECT_FORM_ELEMENT, UserSessionHandler.getController().getFormInUse());

		windowTreeObject.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				if (windowTreeObject.getSelectedTreeObject() instanceof Question) {
					origin = windowTreeObject.getSelectedTreeObject();
					originName.setValue(origin.getName());
					windowTreeObject.close();
				} else {
					MessageManager.showInfo(LanguageCodes.WARNING_DESCRIPTION_RULE_ORIGIN_CAN_ONLY_BE_QUESTIONS);
				}
			}
		});
		windowTreeObject.showCentered();
	}
}
