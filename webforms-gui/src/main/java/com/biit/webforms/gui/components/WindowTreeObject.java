package com.biit.webforms.gui.components;

import com.biit.form.TreeObject;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.language.ILanguageCode;
import com.biit.webforms.gui.webpages.designer.TreeObjectTableDesigner;
import com.biit.webforms.persistence.entity.Form;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;

public class WindowTreeObject extends WindowAcceptCancel {
	private static final long serialVersionUID = -1341380408900400222L;
	private static final String width = "640px";
	private static final String height = "480px";
	private TreeObjectTableDesigner formTable;
	private Class<?>[] selectFilters;

	public WindowTreeObject(ILanguageCode code, Form form, Class<?>... filterClases) {
		super();
		setCaption(code.translation());
		setContent(generateContent(form, filterClases));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	private Component generateContent(Form form, Class<?>... filterClases) {
		formTable = new TreeObjectTableDesigner();
		formTable.setSizeFull();
		formTable.setSelectable(true);
		formTable.loadTreeObject(form, null, filterClases);
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -6136068342027536453L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateAcceptButtonState();
			}
		});
		return formTable;
	}
	
	private void updateAcceptButtonState() {
		if(isSelectAllowed()){
			getAcceptButton().setEnabled(true);
		}else{
			getAcceptButton().setEnabled(false);
		}
	}

	private boolean isSelectAllowed(){
		if(selectFilters==null || selectFilters.length ==0){
			return true;
		}
		if(formTable.getValue()==null){
			return false;
		}
		for(Class<?> filterClass : selectFilters){
			if(filterClass.isInstance(formTable.getValue())){
				return true;
			}
		}
		return false;
	}

	public TreeObject getSelectedTreeObject() {
		return formTable.getSelectedRow();
	}

	public void setSelectableFilers(Class<?>... selectFilters) {
		this.selectFilters = selectFilters;
		updateAcceptButtonState();
	}

}
