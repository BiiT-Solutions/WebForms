package com.biit.webforms.gui.webpages.designer;

import com.biit.form.entity.TreeObject;
import com.biit.webforms.gui.ApplicationUi;
import com.biit.webforms.gui.UserSession;
import com.biit.webforms.gui.components.StorableObjectProperties;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.entity.Group;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PropertiesGroup extends StorableObjectProperties<Group> {
    private static final long serialVersionUID = 2409507883007287631L;
    private static final String WIDTH = "200px";
    private static final int TOTAL_ANSWERS_SELECTED = 2;

    private TextField name;
    private TextArea label;

    private CheckBox repeatable;
    private CheckBox isTable;
    private TextField numberOfColumns;
    private TextField totalAnswersValue;

    public PropertiesGroup() {
        super(Group.class);
    }

    @Override
    protected void initElement() {

        name = new TextField(LanguageCodes.CAPTION_TECHNICAL_NAME.translation());
        name.setWidth(WIDTH);
        name.setRequired(true);
        name.setMaxLength(TreeObject.MAX_UNIQUE_COLUMN_LENGTH);

        label = new TextArea(LanguageCodes.CAPTION_LABEL.translation());
        label.setWidth(WIDTH);
        label.setMaxLength(TreeObject.MAX_LABEL_LENGTH);

        repeatable = new CheckBox(LanguageCodes.CAPTION_REPETABLE.translation());
        isTable = new CheckBox(LanguageCodes.CAPTION_TABLE.translation());

        numberOfColumns = new TextField(LanguageCodes.CAPTION_NUMBER_OF_COLUMNS.translation());
        numberOfColumns.setWidth(WIDTH);
        numberOfColumns.setRequired(true);
        numberOfColumns.setMaxLength(1);

        totalAnswersValue = new TextField(LanguageCodes.CAPTION_TOTAL_ANSWERS_VALUES.translation());
        totalAnswersValue.setWidth(WIDTH);
        totalAnswersValue.setMaxLength(TOTAL_ANSWERS_SELECTED);

        FormLayout commonProperties = new FormLayout();
        commonProperties.setWidth(null);
        commonProperties.setHeight(null);
        commonProperties.addComponent(name);
        commonProperties.addComponent(label);
        commonProperties.addComponent(repeatable);
        commonProperties.addComponent(isTable);
        commonProperties.addComponent(numberOfColumns);
        commonProperties.addComponent(totalAnswersValue);

        boolean canEdit = getWebformsSecurityService().isElementEditable(ApplicationUi.getController().getFormInUse(), UserSession.getUser());
        commonProperties.setEnabled(canEdit);

        addTab(commonProperties, LanguageCodes.CAPTION_PROPERTIES_GROUP.translation(), true);

        super.initElement();
    }

    @Override
    protected void initValues() {
        super.initValues();

        name.addValidator(new ValidatorTreeObjectName(getInstance().getNameAllowedPattern()));
        name.addValidator(new ValidatorDuplicateNameOnSameTreeObjectLevel(getInstance()));
        name.addValidator(new ValidatorTreeObjectNameLength());
        name.setValue(getInstance().getName());
        name.setEnabled(!getInstance().isReadOnly());

        label.setValue(getInstance().getLabel());
        label.addValidator(new LengthValidator(getInstance().getMaxLabelLength()));
        label.setEnabled(!getInstance().isReadOnly());

        repeatable.setValue(getInstance().isRepeatable());
        repeatable.setEnabled(!getInstance().isReadOnly());

        isTable.setValue(getInstance().isShownAsTable());
        isTable.setEnabled(!getInstance().isReadOnly()
                && (!(getInstance().getParent() instanceof Group) || !((Group) getInstance().getParent()).isShownAsTable()));
        isTable.addValidator(new ValidatorNestedTablesNotAllowed(getInstance()));
        isTable.addValidator(new ValidatorTablesAllowesOnlyGroupsAsRows(getInstance()));
        isTable.addValidator(new ValidatorTablesAllowesOnlyQuestionsAsColumns(getInstance()));
        isTable.addValidator(new ValidatorTablesSameColumnsInEachRow(getInstance()));

        numberOfColumns.setValue(getInstance().getNumberOfColumns() + "");
        numberOfColumns.addValidator(new ValidatorInteger());
        numberOfColumns.setEnabled(!getInstance().isReadOnly());
    }

    @Override
    public void updateElement() {
        String tempName = getInstance().getName();
        String tempLabel = getInstance().getLabel();
        if (name.isValid()) {
            tempName = name.getValue();
        }
        if (label.isValid()) {
            tempLabel = label.getValue();
        }

        int numberOfColumnsValue;
        try {
            numberOfColumnsValue = Integer.parseInt(numberOfColumns.getValue());
        } catch (Exception e) {
            numberOfColumnsValue = 1;
        }

        ApplicationUi.getController().updateGroup(getInstance(), tempName, tempLabel, repeatable.getValue(),
                isTable.getValue(), numberOfColumnsValue);

        super.updateElement();
    }

    @Override
    protected void firePropertyUpdateOnExitListener() {
        updateElement();
    }

}
