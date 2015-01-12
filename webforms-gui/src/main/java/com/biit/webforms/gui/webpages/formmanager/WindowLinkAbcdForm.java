package com.biit.webforms.gui.webpages.formmanager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.IBaseFormView;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.utils.BidimensionalHashMap;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class WindowLinkAbcdForm extends WindowAcceptCancel {
	private static final long serialVersionUID = 2256247417807018419L;

	private static final String WINDOW_WIDTH = "40%";
	private static final String WINDOW_HEIGHT = "60%";

	private static final String BOXED_LIST_LAYOUT = "v-boxed-list";

	private static final String SELECTION_BOX_WIDTH = "150px";

	private TableSimpleFormNameOrganization abcdFormsTable;
	private OptionGroup versionList;
	private BidimensionalHashMap<String, Long, ArrayList<IBaseFormView>> abcdForms;

	public WindowLinkAbcdForm() {
		super();
		abcdForms = new BidimensionalHashMap<>();
		configure();
		setContent(generateContent());
	}

	public void add(IBaseFormView form) {
		if (!abcdForms.containsKey(form.getLabel(), form.getOrganizationId())) {
			// Add to table and initialize a new list
			abcdFormsTable.add(form);
			abcdForms.put(form.getLabel(), form.getOrganizationId(), new ArrayList<IBaseFormView>());
		}
		abcdForms.get(form.getLabel(), form.getOrganizationId()).add(form);
	}
	
	public void setValue(List<SimpleFormView> linkedSimpleAbcdForms) {
		if(linkedSimpleAbcdForms!=null && !linkedSimpleAbcdForms.isEmpty()){
			abcdFormsTable.setValue(getTableValue(linkedSimpleAbcdForms.get(0)));
			versionList.setValue(new HashSet<SimpleFormView>(linkedSimpleAbcdForms));
		}
	}

	private IBaseFormView getTableValue(SimpleFormView simpleFormView) {
		if(abcdForms.containsKey(simpleFormView.getLabel(), simpleFormView.getOrganizationId())){
			return abcdForms.get(simpleFormView.getLabel(), simpleFormView.getOrganizationId()).get(0);
		}
		return null;
	}

	private Component generateContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		abcdFormsTable = generateFormsTable();
		versionList = generateVersionList();

		abcdFormsTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2575871790271819882L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateVersionList();
			}
		});

		VerticalLayout versionListLayout = new VerticalLayout();
		versionListLayout.addStyleName(BOXED_LIST_LAYOUT);
		versionListLayout.addComponent(versionList);
		versionListLayout.setWidth(SELECTION_BOX_WIDTH);
		versionListLayout.setHeight("100%");

		rootLayout.addComponent(abcdFormsTable);
		rootLayout.addComponent(versionListLayout);
		
		rootLayout.setExpandRatio(abcdFormsTable, 1.0f);

		return rootLayout;
	}

	protected void updateVersionList() {
		versionList.removeAllItems();
		versionList.setValue(null);
		if (abcdFormsTable.getValue() != null) {
			IBaseFormView selectedForm = ((IBaseFormView) abcdFormsTable.getValue());
			for (IBaseFormView form : abcdForms.get(selectedForm.getLabel(), selectedForm.getOrganizationId())) {
				addToVersionList(form);
			}
		}
	}

	private void addToVersionList(IBaseFormView form) {
		versionList.addItem(form);
		String versionCaption = LanguageCodes.CAPTION_VERSION.translation() + ": " + form.getVersion();
		versionList.setItemCaption(form, versionCaption);
	}

	private OptionGroup generateVersionList() {
		OptionGroup list = new OptionGroup();
		list.setWidth("100%");
		list.setHeight(null);
		list.setMultiSelect(true);
		list.setImmediate(true);

		return list;
	}

	private TableSimpleFormNameOrganization generateFormsTable() {
		TableSimpleFormNameOrganization table = new TableSimpleFormNameOrganization();
		table.setSizeFull();
		table.setSelectable(true);
		table.setImmediate(true);

		return table;
	}

	private void configure() {
		setDraggable(false);
		setModal(true);
		setResizable(false);

		setWidth(WINDOW_WIDTH);
		setHeight(WINDOW_HEIGHT);
	}

	public Set<IBaseFormView> getValue() {
		Set<IBaseFormView> value = new HashSet<IBaseFormView>();
		if(versionList.getValue()!=null){
			for(Object selectedElement: ((Set<?>)versionList.getValue())){
				value.add((IBaseFormView) selectedElement);
			}
		}
		return value;
	}
}
