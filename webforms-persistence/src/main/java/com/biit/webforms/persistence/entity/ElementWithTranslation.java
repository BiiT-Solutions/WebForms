package com.biit.webforms.persistence.entity;

import java.util.Map;

public interface ElementWithTranslation {

    void setLabelTranslations(Map<String, String> labelTranslations);

    Map<String, String> getLabelTranslations();

    Map<String, String> getDescriptionTranslations();

    void setDescriptionTranslations(Map<String, String> descriptionTranslations);

}
