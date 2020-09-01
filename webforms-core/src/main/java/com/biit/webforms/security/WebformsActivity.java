package com.biit.webforms.security;

import com.biit.usermanager.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and
 * cannot do. The string for each activity must be unique.
 */
public enum WebformsActivity implements IActivity {
	READ("Read"),

	BUILDING_BLOCK_EDITING("BuildingBlockEditing"),

	BUILDING_BLOCK_FLOW_EDITING("BuildingBlockFLowEditing"),

	BUILDING_BLOCK_ADD_FROM_FORM("BuildingBlockAdd"),

	FORM_EDITING("FormEditing"),

	FORM_FLOW_EDITING("FormFlowEditing"),

	FORM_STATUS_UPGRADE("FormStatusUpgrade"),

	FORM_STATUS_DOWNGRADE("FormStatusDowngrade"),

	FORM_NEW_VERSION("CreateNewFormVersion"),

	XFORMS_EXPORT("XFormsExport"),

	XFORMS_PUBLISH("XFormsPublish"),

	XML_VALIDATOR_AGAINST_FORM("XmlValidatorAgainstForm"),

	FORM_ANALYSIS("FormAnalysis"),

	ADMIN_FORMS("AdminRights"),

	EVICT_CACHE("EvictCache"),

	FORM_REMOVE("FormRemove"),

	BLOCK_REMOVE("BlockRemove"),

	IMPORT_JSON("ImportJson"),

	EXPORT_JSON("ExportJson"),

	EXPORT_ABCD("ExportAbcd"),

	EXPORT_TO_CSV("ExportToCsv"),

	;

	private String tag;

	WebformsActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}
