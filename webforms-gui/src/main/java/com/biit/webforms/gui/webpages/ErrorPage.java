package com.biit.webforms.gui.webpages;

import com.biit.webforms.gui.common.components.WebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ErrorPage extends WebPageComponent {
	private static final long serialVersionUID = -587681959593342489L;
	private static final String TEXT_WIDTH = "300px";
	private static final String DEFAULT_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sed fermentum odio, a sodales ipsum. Cras ut purus dolor. Aliquam vel risus turpis. Sed eu facilisis magna, et porta magna. Aliquam ut tempus ante. Maecenas ac eros urna. Proin a erat eros. Curabitur tincidunt dictum venenatis. Proin vestibulum ligula a mi congue auctor. Donec et elit facilisis, pellentesque nisi in, tincidunt ipsum. Nullam a lectus convallis, convallis mi non, rhoncus felis. Mauris ut fringilla neque";
	private VerticalLayout rootLayout;

	private VerticalLayout textLayout;
	private Label label;

	public ErrorPage() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();

		textLayout = new VerticalLayout();
		textLayout.setWidth(TEXT_WIDTH);
		textLayout.setHeight(null);

		label = new Label(DEFAULT_TEXT);

		textLayout.addComponent(label);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}

}
