package com.biit.webforms.gui.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.components.IconButton;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.common.components.WindowAcceptCancel;
import com.biit.webforms.gui.common.components.WindowAcceptCancel.AcceptActionListener;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.components.FormEditBottomMenu;
import com.biit.webforms.gui.components.FormFlowViewer;
import com.biit.webforms.gui.webpages.floweditor.EditItemAction;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField;
import com.biit.webforms.gui.webpages.floweditor.SearchFormElementField.SearchFormElementChanged;
import com.biit.webforms.gui.webpages.floweditor.TableRules;
import com.biit.webforms.gui.webpages.floweditor.TableRules.NewItemAction;
import com.biit.webforms.gui.webpages.floweditor.UpperMenuFlowEditor;
import com.biit.webforms.gui.webpages.floweditor.WindowRule;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.theme.ThemeIcons;
import com.biit.webforms.utils.GraphvizApp.ImgType;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

public class FlowEditor extends SecuredWebPage {
	private static final long serialVersionUID = -6257723403353946354L;
	private static final List<IActivity> activityPermissions = new ArrayList<IActivity>(
			Arrays.asList(WebformsActivity.READ));
	private static final double ZOOM_MIN_VALUE = 1.0f;
	private static final double ZOOM_MAX_VALUE = 50.0f;

	private UpperMenuFlowEditor upperMenu;
	private TableRules tableRules;
	private FormFlowViewer formFlowViewer;
	private SearchFormElementField tableFilterOrigin;
	private SearchFormElementField tableFilterDestiny;
	private SearchFormElementField flowViewerFilter;
	private Slider zoomSlider;

	private OriginFilter originFilter;
	private OriginFilter destinyFilter;

	@Override
	protected void initContent() {
		setCentralPanelAsWorkingArea();
		upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
		setBottomMenu(new FormEditBottomMenu());
		getWorkingArea().addComponent(generateContent());
		updateUiState();
	}

	private Component generateContent() {
		HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
		horizontalSplitPanel.setSizeFull();
		horizontalSplitPanel.setFirstComponent(createLeftComponent());
		horizontalSplitPanel.setSecondComponent(createRightComponent());
		horizontalSplitPanel.setSplitPosition(50.0f);

		return horizontalSplitPanel;
	}

	private Component createLeftComponent() {
		VerticalLayout i = new VerticalLayout();
		i.setSizeFull();

		Component tableFilterBar = createTableFilterBar();

		tableRules = new TableRules();
		tableRules.setMultiSelect(true);
		tableRules.setSizeFull();
		tableRules.addNewItemActionListener(new NewItemAction() {

			@Override
			public void newItemAction() {
				addNewRuleAction();
			}
		});
		tableRules.addEditItemActionListener(new EditItemAction() {

			@Override
			public void editItemAction(Rule rule) {
				editRuleAction(rule);
			}
		});
		tableRules.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4525037694598967266L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUiState();
			}
		});
		Set<Rule> rules = UserSessionHandler.getController().getFormInUse().getRules();
		tableRules.addRows(rules);

		i.addComponent(tableFilterBar);
		i.addComponent(tableRules);
		i.setExpandRatio(tableRules, 1.0f);

		return i;
	}

	public class OriginFilter implements Filter {
		private static final long serialVersionUID = -8541390160081477981L;

		protected TreeObject filter;
		protected Object newRule;

		public OriginFilter(TreeObject filter, Object newRule) {
			super();
			this.filter = filter;
			this.newRule = newRule;
		}

		public TreeObject getFilter() {
			return filter;
		}

		public void setFilter(TreeObject filter) {
			this.filter = filter;
		}

		@Override
		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
			if (filter == null || (itemId != null && itemId.equals(newRule))) {
				return true;
			}
			Rule rule = (Rule) itemId;
			return filter.equals(rule.getOrigin()) || filter.contains(rule.getOrigin());
		}

		@Override
		public boolean appliesToProperty(Object propertyId) {
			// Doesn't apply to any property.
			return false;
		}
	};

	public class DestinyFilter extends OriginFilter {
		private static final long serialVersionUID = -7194892064324001003L;

		public DestinyFilter(TreeObject filter, Object newRule) {
			super(filter, newRule);
		}

		@Override
		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
			if (filter == null || (itemId != null && itemId.equals(newRule))) {
				return true;
			}
			Rule rule = (Rule) itemId;
			return filter.equals(rule.getDestiny()) || filter.contains(rule.getDestiny());
		}
	}

	private Component createTableFilterBar() {
		HorizontalLayout i = new HorizontalLayout();
		i.setMargin(true);
		i.setSpacing(true);
		i.setWidth("100%");

		tableFilterOrigin = new SearchFormElementField();
		tableFilterOrigin.setCaption(LanguageCodes.CAPTION_FILTER_ORIGIN.translation());
		tableFilterOrigin.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				Filterable f = (Filterable) tableRules.getContainerDataSource();
				if (originFilter != null) {
					f.removeContainerFilter(originFilter);
				}
				originFilter = new OriginFilter((TreeObject) tableFilterOrigin.getValue(), tableRules.getNewRuleId());
				f.addContainerFilter(originFilter);
			}
		});
		tableFilterDestiny = new SearchFormElementField();
		tableFilterDestiny.setCaption(LanguageCodes.CAPTION_FILTER_DESTINY.translation());
		tableFilterDestiny.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				Filterable f = (Filterable) tableRules.getContainerDataSource();
				if (destinyFilter != null) {
					f.removeContainerFilter(destinyFilter);
				}
				destinyFilter = new DestinyFilter((TreeObject) tableFilterDestiny.getValue(), tableRules.getNewRuleId());
				f.addContainerFilter(destinyFilter);
			}
		});

		i.addComponent(tableFilterOrigin);
		i.addComponent(tableFilterDestiny);
		i.setExpandRatio(tableFilterOrigin, 0.5f);
		i.setExpandRatio(tableFilterDestiny, 0.5f);
		i.setComponentAlignment(tableFilterOrigin, Alignment.MIDDLE_LEFT);
		i.setComponentAlignment(tableFilterDestiny, Alignment.MIDDLE_RIGHT);

		return i;
	}

	private void updateUiState() {

		boolean selectedNew = false;
		@SuppressWarnings("unchecked")
		Set<Object> itemIds = (Set<Object>) tableRules.getValue();
		boolean multipleSelection = itemIds.size() > 1;
		for (Object itemId : itemIds) {
			selectedNew = itemId != null && itemId.equals(tableRules.getNewRuleId());
		}
		boolean canEdit = WebformsAuthorizationService.getInstance().isFormEditable(
				UserSessionHandler.getController().getFormInUse(), UserSessionHandler.getUser());

		// Top button state
		upperMenu.getSaveButton().setEnabled(canEdit);
		upperMenu.getNewRuleButton().setEnabled(canEdit);
		upperMenu.getEditRuleButton().setEnabled(canEdit && !selectedNew && !multipleSelection);
		upperMenu.getCloneRuleButton().setEnabled(canEdit && !selectedNew);
		upperMenu.getRemoveRuleButton().setEnabled(canEdit && !selectedNew);
		upperMenu.getValidateButton().setEnabled(false);
		upperMenu.getFinishButton().setEnabled(canEdit);
	}

	private Component createRightComponent() {
		VerticalLayout i = new VerticalLayout();
		i.setSizeFull();

		formFlowViewer = new FormFlowViewer(ImgType.SVG, 1.0f);
		formFlowViewer.setSizeFull();
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getFormInUse(), null);

		Component flowViewerControlBar = createFlowViewerControlBar();

		i.addComponent(flowViewerControlBar);
		i.addComponent(formFlowViewer);
		i.setExpandRatio(formFlowViewer, 1.0f);

		return i;
	}

	private Component createFlowViewerControlBar() {
		HorizontalLayout i = new HorizontalLayout();
		i.setMargin(true);
		i.setSpacing(true);
		i.setWidth("100%");

		flowViewerFilter = new SearchFormElementField(Form.class, Category.class, Group.class);
		flowViewerFilter.setCaption(LanguageCodes.CAPTION_FILTER.translation());
		flowViewerFilter.setSelectableFilter(Category.class, Group.class);
		flowViewerFilter.addValueChangeListener(new SearchFormElementChanged() {

			@Override
			public void currentElement(Object object) {
				filterFlowDiagram((TreeObject) object);
			}
		});

		IconButton redrawButton = new IconButton(LanguageCodes.CAPTION_REDRAW, ThemeIcons.REDRAW,
				LanguageCodes.TOOLTIP_REDRAW);
		redrawButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7689117025171099202L;

			@Override
			public void buttonClick(ClickEvent event) {
				formFlowViewer.setZoom(1.0f);
				formFlowViewer.redraw();
			}
		});

		zoomSlider = new Slider();
		zoomSlider.setCaption(LanguageCodes.CAPTION_ZOOM_SLIDER.translation());
		zoomSlider.setMin(ZOOM_MIN_VALUE);
		zoomSlider.setMax(ZOOM_MAX_VALUE);
		zoomSlider.setValue(ZOOM_MIN_VALUE);
		zoomSlider.setWidth("100%");
		zoomSlider.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5649221005286915625L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				double currentValue = (double) event.getProperty().getValue();
				formFlowViewer.setZoom((float) currentValue);
			}
		});

		i.addComponent(flowViewerFilter);
		i.addComponent(redrawButton);
		i.addComponent(zoomSlider);
		i.setExpandRatio(flowViewerFilter, 0.4f);
		i.setExpandRatio(zoomSlider, 0.4f);
		i.setComponentAlignment(flowViewerFilter, Alignment.MIDDLE_LEFT);
		i.setComponentAlignment(redrawButton, Alignment.BOTTOM_CENTER);
		i.setComponentAlignment(zoomSlider, Alignment.MIDDLE_RIGHT);

		return i;
	}

	protected void filterFlowDiagram(TreeObject filter) {
		zoomSlider.setValue(ZOOM_MIN_VALUE);
		formFlowViewer.setFormAndFilter(UserSessionHandler.getController().getFormInUse(), filter);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	private UpperMenuFlowEditor createUpperMenu() {
		UpperMenuFlowEditor upperMenu = new UpperMenuFlowEditor();
		upperMenu.addSaveButtonListener(new ClickListener() {
			private static final long serialVersionUID = 1679355377155929573L;

			@Override
			public void buttonClick(ClickEvent event) {
				UserSessionHandler.getController().saveForm();
				MessageManager.showInfo(LanguageCodes.INFO_MESSAGE_CAPTION_SAVE,
						LanguageCodes.INFO_MESSAGE_DESCRIPTION_SAVE);
			}
		});
		upperMenu.addNewRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@Override
			public void buttonClick(ClickEvent event) {
				addNewRuleAction();
			}
		});
		upperMenu.addEditRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = 9116553626932253896L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				if (tableRules != null) {
					Rule selectedRule = null;
					if(tableRules.getValue() instanceof Rule){
						selectedRule = (Rule) tableRules.getValue(); 
					}
					if(tableRules.getValue() instanceof Set<?>){
						selectedRule = (Rule)((Set<Object>)tableRules.getValue()).iterator().next();
					}
					editRuleAction(selectedRule);
				}
			}
		});
		upperMenu.addCloneRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = -151662907240066702L;

			@Override
			public void buttonClick(ClickEvent event) {
				@SuppressWarnings("unchecked")
				Set<Object> selectedObjects = (Set<Object>) tableRules.getValue();
				if (selectedObjects != null) {
					Set<Rule> selectedRules = new HashSet<Rule>();
					for (Object selectedObject : selectedObjects) {
						if (selectedObject instanceof Rule) {
							selectedRules.add((Rule) selectedObject);
						}
					}
					Set<Rule> clones = UserSessionHandler.getController().cloneRulesAndInsertIntoForm(selectedRules);
					addOrUpdateRuleInTableAction(clones.toArray(new Rule[0]));
				}
			}
		});
		upperMenu.addRemoveRuleButtonListener(new ClickListener() {
			private static final long serialVersionUID = -5479947134511433646L;

			@Override
			public void buttonClick(ClickEvent event) {
				@SuppressWarnings("unchecked")
				Set<Object> selectedObjects = (Set<Object>) tableRules.getValue();
				Set<Rule> selectedRules = new HashSet<Rule>();
				for (Object selectedObject : selectedObjects) {
					if (selectedObject instanceof Rule) {
						selectedRules.add((Rule) selectedObject);
					}
				}
				UserSessionHandler.getController().removeRules(selectedRules);
				removeRulesFromTable(selectedRules);
			}
		});
		upperMenu.addValidateButtonListener(new ClickListener() {
			private static final long serialVersionUID = -1627616225877959507L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addFinishButtonListener(new ClickListener() {
			private static final long serialVersionUID = 8869180038869702710L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});

		return upperMenu;
	}

	/**
	 * This method opens the new rule window
	 */
	protected void addNewRuleAction() {
		WindowRule window = new WindowRule();
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowRule) window).getRule();
				addOrUpdateRuleInTableAction(newRule);
				window.close();
			}
		});
		window.showCentered();
	}

	/**
	 * This method takes a existing rule and opens rule window with the
	 * parameters assigned in the rule to edit.
	 * 
	 * @param rule
	 */
	protected void editRuleAction(Rule rule) {
		WindowRule window = new WindowRule();
		window.setRule(rule);
		window.addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(WindowAcceptCancel window) {
				Rule newRule = ((WindowRule) window).getRule();
				addOrUpdateRuleInTableAction(newRule);
				window.close();
			}
		});
		window.showCentered();
	}

	private void addOrUpdateRuleInTableAction(Rule... newRules) {
		tableRules.addOrUpdateRules(newRules);
	}
	
	private void removeRulesFromTable(Set<Rule> rules){
		for(Rule rule: rules){
			tableRules.removeItem(rule);
		}
	}
}
